package com.mycuckoo.gateway.filter;

import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.util.JsonUtils;
import com.mycuckoo.core.util.StrUtils;
import com.mycuckoo.core.util.web.InetUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.mycuckoo.constant.BaseConst.SESSION_USER_INFO;

public class LoggingFilter implements GlobalFilter, Ordered {
    private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    /**
     * 顺序必须是 < -1，否则标准的NettyWriteResponseFilter将在您的过滤器得到一个被调用的机会之前发送响应
     * 也就是说如果不小于 -1 ，将不会执行获取后端响应的逻辑
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -10;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MediaType mediaType = request.getHeaders().getContentType();
        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
            return logBody(exchange, chain);
        } else {
            return logBasic(exchange, chain);
        }
    }

    private Mono<Void> logBasic(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        UserInfo user = exchange.getAttribute(SESSION_USER_INFO);
        logRequest(exchange.getRequest(), user, request.getQueryParams().isEmpty() ? "empty" : JsonUtils.toJson(request.getQueryParams()));

        //获取响应体
        ServerHttpResponseDecorator response = decorateResponse(exchange);
        return chain.filter(exchange.mutate().response(response).build());
    }

    /**
     * 解决 request body 只能读取一次问题，
     * 参考: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     */
    private Mono logBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerRequest request = ServerRequest.create(exchange, messageReaders);
        Mono<String> modifiedBody = request.bodyToMono(String.class).flatMap(body -> {
            logRequest(exchange.getRequest(), exchange.getAttribute(SESSION_USER_INFO), body == null || body.equals("") ? "empty" : body);
            return Mono.just(body);
        });

        // 通过 BodyInserter 插入 body(支持修改body), 避免 request body 只能获取一次
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // 重新装饰请求
                    ServerHttpRequest decoratedRequest = decorateRequest(exchange, headers, outputMessage);

                    // 重新装饰响应
                    ServerHttpResponseDecorator decoratedResponse = decorateResponse(exchange);

                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build());
                }));
    }

    /**
     * 请求装饰器，重新计算 headers
     */
    private ServerHttpRequestDecorator decorateRequest(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    // TODO: this causes a 'HTTP/1.1 411 Length Required' // on
                    // httpbin.org
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题
     */
    private ServerHttpResponseDecorator decorateResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    // 获取响应类型，如果是 json 就打印
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (Objects.equals(this.getStatusCode(), HttpStatus.OK)
                            && StrUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains("application/json")) {

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            // 释放掉内存
                            DataBufferUtils.release(join);

                            logResponse(new String(content, StandardCharsets.UTF_8));

                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }

    private void logRequest(ServerHttpRequest request, UserInfo user, String body) {
        String ip = InetUtils.getIp(request);
        String usrId = user == null ? "" : user.getId().toString();
        String usrName = user == null ? "" : user.getUserName();
        String url = request.getURI().getPath();
        logger.info("in, {}|{}|{}|{}|{}", ip, usrId, usrName, url, body);
    }

    private void logResponse(String response) {
        logger.info("out, {}", response);
    }
}
