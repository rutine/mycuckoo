package com.mycuckoo.gateway.exception;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);


    public WebExceptionHandler(ErrorAttributes attributes, WebProperties properties, ApplicationContext applicationContext, ServerCodecConfigurer codecConfigurer) {
        super(attributes, properties.getResources(), applicationContext);
        super.setMessageWriters(codecConfigurer.getWriters());
        super.setMessageReaders(codecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::getResponse);
    }

    private Mono<ServerResponse> getResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        logger.error("error, {}", JsonUtils.toJson(errorAttributes));

        Throwable error = getError(request);
        BodyInserter<AjaxResponse, ReactiveHttpOutputMessage> result;
        if (error instanceof UnauthorizedException) {
            result = BodyInserters.fromValue(AjaxResponse.success(((UnauthorizedException) error).getMsg()));
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(result);
        } else if (error instanceof ForbiddenException) {
            result = BodyInserters.fromValue(AjaxResponse.success(((ForbiddenException) error).getMsg()));
            return ServerResponse.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(result);
        } else if (error instanceof ResponseStatusException) {
            HttpStatus status = HttpStatus.resolve(((ResponseStatusException) error).getStatusCode().value());
            if (status == HttpStatus.NOT_FOUND) {
                logger.warn("not found, uri={} msg={}", request.path(), request.attributes().toString());
                return Mono.error(error);
            } else if (status == HttpStatus.TOO_MANY_REQUESTS) {
                result = BodyInserters.fromValue(AjaxResponse.create(HttpStatus.TOO_MANY_REQUESTS.value(), "请求频繁"));
            } else {
                logger.error("proxied service occur error!", error);
                result = BodyInserters.fromValue(AjaxResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务错误"));
            }

            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(result);
        } else {
            result = BodyInserters.fromValue(AjaxResponse.success("服务不可用"));
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(result);
        }
    }

}
