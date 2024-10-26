package com.mycuckoo.gateway.filter;

import com.mycuckoo.core.util.IdGenerator;
import com.mycuckoo.gateway.config.FeignConfiguration;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class TracerFilter implements GlobalFilter, Ordered {
    private static final String REQUEST_ID = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID);
        if (StringUtils.isBlank(requestId)) {
            requestId = exchange.getRequest().getId();
        }
        if (StringUtils.isBlank(requestId)) {
            requestId = IdGenerator.uuid();
        }
        MDC.put("traceId", requestId);

        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(REQUEST_ID, requestId).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        FeignConfiguration.setHttpHeaders(exchange.getRequest().getHeaders());

        return chain.filter(newExchange).doFinally((signalType) -> {
            FeignConfiguration.clearHttpHeaders();
            MDC.remove("traceId");
        });
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
