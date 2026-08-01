package com.mycuckoo.gateway.filter;

import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.util.JsonUtils;
import com.mycuckoo.core.util.web.InetUtils;
import com.mycuckoo.gateway.handle.predicate.ReadBodyRoutePredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mycuckoo.core.constant.BaseConst.SESSION_USER_INFO;


public class LoggingOldFilter implements GlobalFilter, Ordered {
    final Logger logger = LoggerFactory.getLogger(LoggingOldFilter.class);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        UserInfo userInfo = exchange.getAttribute(SESSION_USER_INFO);

        String ip = InetUtils.getIp(request);
        String userId = userInfo == null ? "" : userInfo.getId().toString();
        String userName = userInfo == null ? "" : userInfo.getUserName();
        String url = request.getURI().getPath();
        String body = "empty";

        MediaType contentType = request.getHeaders().getContentType();
        return Mono.fromSupplier(() -> {
            try {
                if (request.getMethod() == HttpMethod.GET) {
                    return Mono.just(request.getQueryParams().isEmpty() ? body : JsonUtils.toJson(request.getQueryParams()));
                }
                else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                    String bodyObj = exchange.getAttribute(ReadBodyRoutePredicateFactory.CACHE_REQUEST_BODY_OBJECT_KEY);
                    return Mono.just(bodyObj == null ? body : bodyObj);
                }
                else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
                    return exchange.getFormData().map(map -> {
                        if (!map.isEmpty()) {
                            return JsonUtils.toJson(map);
                        } else {
                            throw new RuntimeException("");
                        }
                    }).onErrorResume(fallback -> {
                        return exchange.getRequest().getBody().map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            try {
                                return new String(bytes, "utf-8");
                            } catch (UnsupportedEncodingException e) {

                            }
                            return body;
                        }).next();
                    });
                }
                else if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
                    return exchange.getMultipartData().map(map -> {
                        Map<String, List<String>> fieldMap = new HashMap<>();
                        map.forEach((name, values) -> {
                            List<String> vals = new ArrayList<>();
                            for (Part part : values) {
                                if (part instanceof FormFieldPart) {
                                    vals.add(((FormFieldPart) part).value());
                                }
                            }
                            if (vals.isEmpty()) {
                                fieldMap.put(name, vals);
                            }
                        });
                        return fieldMap.isEmpty() ? body : JsonUtils.toJson(fieldMap);
                    });
                }
                else {
                    String bodyObj = exchange.getAttribute(ReadBodyRoutePredicateFactory.CACHE_REQUEST_BODY_OBJECT_KEY);
                    return Mono.just(bodyObj == null ? body : bodyObj);
                }
            } catch (Exception e) {
                logger.error("debug log error", e);
            }

            return Mono.just(body);
        }).flatMap((data) -> {
            data.map(json -> {
                try {
                    MDC.put("traceId", exchange.getRequest().getId());
                    String result = trimJson(json);
//                    if (logMasker != null) {
//                        result = logMasker.mask(result);
//                    }
                    logger.info("{}|{}|{}|{}|{}", ip, userId, userName, url, result);
                } catch (Exception e) {
                    logger.error("print request log error", e);
                }

                return json;
            }).subscribe();

            return chain.filter(exchange);
        });
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private String trimJson(String json) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = json.length(); i < len; i++) {
            char ch = json.charAt(i);
            if (ch == ' ') {
                continue;
            } else if (ch == '\n' || ch == '\t' || ch == '\r') {
                builder.append(' ');
                continue;
            }
            builder.append(ch);
        }

        return builder.toString();
    }
}
