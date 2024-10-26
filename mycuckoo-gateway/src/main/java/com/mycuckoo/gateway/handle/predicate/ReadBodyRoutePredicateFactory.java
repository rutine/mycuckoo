package com.mycuckoo.gateway.handle.predicate;

import com.google.common.collect.Lists;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class ReadBodyRoutePredicateFactory extends org.springframework.cloud.gateway.handler.predicate.ReadBodyRoutePredicateFactory {
    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";
    private static final String TEST_ATTRIBUTE = "read_body_predicate_test_attribute";

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    @Override
    public AsyncPredicate<ServerWebExchange> applyAsync(final Config config) {
        return new AsyncPredicate<ServerWebExchange>() {
            public Publisher<Boolean> apply(ServerWebExchange exchange) {
                //扩展部分, 缓存指定类型的内容. 如不缓存文件
                ServerHttpRequest request = exchange.getRequest();
                if (HttpMethod.GET == request.getMethod()
                        || request.getHeaders().getContentLength() == 0) {
                    return Mono.just(true);
                }
                MediaType mediaType = request.getHeaders().getContentType();
                if (mediaType != null
                        && !mediaType.toString().contains("x-www-form-urlencoded")
                        && !mediaType.toString().contains("json")
                        && !mediaType.toString().contains("xml")
                        && !mediaType.toString().contains("text")) {
                    return Mono.just(true);
                }

                //复用原有部分
                Class inClass = config.getInClass();
                Object cachedBody = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);
                if (cachedBody != null) {
                    try {
                        boolean test = config.getPredicate().test(cachedBody);
                        exchange.getAttributes().put(TEST_ATTRIBUTE, test);
                        return Mono.just(test);
                    } catch (ClassCastException var6) {
                        if (ReadBodyRoutePredicateFactory.log.isDebugEnabled()) {
                            ReadBodyRoutePredicateFactory.log.debug("Predicate test failed because class in predicate does not match the cached body object", var6);
                        }

                        return Mono.just(false);
                    }
                } else {
                    return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (serverHttpRequest) -> {
                        return ServerRequest.create(exchange.mutate().request(serverHttpRequest).build(), serverCodecConfigurer.getReaders()).bodyToMono(inClass).doOnNext((objectValue) -> {
                            exchange.getAttributes().put(CACHE_REQUEST_BODY_OBJECT_KEY, objectValue);
                        }).map((objectValue) -> {
                            return config.getPredicate().test(objectValue);
                        });
                    });
                }
            }

            public String toString() {
                return String.format("ReadBody: %s", config.getInClass());
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Lists.newArrayList("inClass", "predicate");
    }
}
