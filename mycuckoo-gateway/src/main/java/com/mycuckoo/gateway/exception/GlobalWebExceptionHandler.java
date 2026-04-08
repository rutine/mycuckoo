package com.mycuckoo.gateway.exception;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

//@Order(-1)
//@Component
public class GlobalWebExceptionHandler implements WebExceptionHandler {
    public static final byte[] ERROR_MSG = JsonUtils.toJson(AjaxResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务不可用")).getBytes(StandardCharsets.UTF_8);

    private static Logger logger = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        MDC.put("traceId", exchange.getRequest().getId());
        if (response.isCommitted()) {
            logger.warn("response is committed");
            return Mono.empty();
        }

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            HttpStatus httpStatus = HttpStatus.resolve(((ResponseStatusException) ex).getStatusCode().value());
            AjaxResponse result;
            if (httpStatus == HttpStatus.NOT_FOUND) {
                logger.warn("not found, uri={} msg={}", exchange.getRequest().getPath().value(), exchange.getAttributes().toString());
               return Mono.error(ex);
            } else if (httpStatus == HttpStatus.TOO_MANY_REQUESTS) {
                result = AjaxResponse.create(HttpStatus.TOO_MANY_REQUESTS.value(), "请求频繁");
            } else {
                logger.error("proxied service occur error!", ex);
                result = AjaxResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务错误");
            }

            return response.writeWith(Mono.fromSupplier(() -> {
                MDC.put("traceId", exchange.getRequest().getId());
                DataBufferFactory bufferFactory = response.bufferFactory();
                try {
                    return bufferFactory.wrap(JsonUtils.toJson(result).getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    logger.error("response error!", e);
                    return bufferFactory.wrap(ERROR_MSG);
                }
            })).then(Mono.defer(response::setComplete));
        }

        logger.error("system error found!", ex);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(ERROR_MSG);
        })).then(Mono.defer(response::setComplete));
    }
}
