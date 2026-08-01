package com.mycuckoo.gateway.config;

import com.mycuckoo.core.autoconfig.FeignLoggerProperties;
import feign.MethodMetadata;
import feign.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpResponse;
import reactivefeign.client.log.ReactiveLoggerListener;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MyReactiveLoggerListener implements ReactiveLoggerListener<MyReactiveLoggerListener.LogContext> {

    private static final String BINARY_DATA = "Binary data";

    private final Logger logger = LoggerFactory.getLogger(MyReactiveLoggerListener.class);
    private final FeignLoggerProperties properties;

    public MyReactiveLoggerListener(FeignLoggerProperties properties) {
        this.properties = properties;
    }

    @Override
    public LogContext requestStarted(ReactiveHttpRequest request, Target<?> target, MethodMetadata methodMetadata) {
        LogContext context = new LogContext(request, methodMetadata != null ? methodMetadata.configKey() : request.methodKey());
        if (!properties.isEnabled()) {
            return context;
        }

        context.setRequestMessage(new StringBuilder()
                .append(request.method())
                .append(" ")
                .append(safeUri(request.uri())));

        if (properties.isIncludeHeaders() && request.headers() != null && !request.headers().isEmpty()) {
            context.getRequestMessage().append(", headers=").append(formatHeaders(request.headers()));
        }

        if (!logRequestBody()) {
            logInfo(context.getConfigKey(), "---> %s", context.getRequestMessage());
            context.setRequestLogged(true);
        }
        return context;
    }

    @Override
    public boolean logRequestBody() {
        return properties.isEnabled() && properties.isIncludePayload();
    }

    @Override
    public void bodySent(Object body, LogContext context) {
        if (!logRequestBody()) {
            return;
        }
        String payload = abbreviateBody(body);
        StringBuilder message = context.getRequestMessage() != null ? context.getRequestMessage() : new StringBuilder();
        message.append(", size=").append(formatContentSize(payloadSize(payload)))
                .append(", payload=").append(payload);
        logInfo(context.getConfigKey(), "---> %s", message);
        context.setRequestLogged(true);
    }

    @Override
    public void responseReceived(ReactiveHttpResponse<?> response, LogContext context) {
        context.setResponse(response);
        if (!properties.isEnabled()) {
            return;
        }

        context.setResponseMessage(new StringBuilder()
                .append(response.status())
                .append(" ")
                .append(formatElapsed(context.elapsedMillis())));
        if (!logResponseBody()) {
            logInfo(context.getConfigKey(), "<--- %s", context.getResponseMessage());
            context.setResponseLogged(true);
        }
    }

    @Override
    public void errorReceived(Throwable throwable, LogContext context) {
        if (!properties.isEnabled()) {
            return;
        }
        logger.error(methodTag(context.getConfigKey()) + "<--- error, url={}", safeUri(context.getRequest().uri()), throwable);
    }

    @Override
    public boolean logResponseBody() {
        return properties.isEnabled() && properties.isIncludePayload();
    }

    @Override
    public void bodyReceived(Object body, LogContext context) {
        if (!logResponseBody()) {
            return;
        }
        String payload = abbreviateBody(body);
        StringBuilder message = context.getResponseMessage() != null ? context.getResponseMessage() : new StringBuilder();
        message.append(", size=").append(formatContentSize(payloadSize(payload)))
                .append(", payload=").append(payload);
        logInfo(context.getConfigKey(), "<--- %s", message);
        context.setResponseLogged(true);
    }

    private String safeUri(URI uri) {
        return uri == null ? "" : uri.toString();
    }

    private String formatHeaders(Map<String, List<String>> headers) {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        headers.forEach((name, values) -> joiner.add(name + "=" + values));
        return joiner.toString();
    }

    private String abbreviateBody(Object body) {
        if (body == null) {
            return "null";
        }

        String value = String.valueOf(body);
        if (value.isBlank()) {
            return value;
        }

        int maxSize = Math.max(properties.getMaxPayloadSize(), 16);
        int originalLength = value.length();
        if (originalLength <= maxSize) {
            return value;
        }
        return value.substring(0, maxSize) + "...(" + originalLength + " chars)";
    }

    private String formatContentSize(int size) {
        double k = 1024D;
        double humanSize = size;
        if (humanSize < k) {
            return size + "B";
        }
        if ((humanSize /= k) < k) {
            return String.format("%.2fK", humanSize);
        }
        if ((humanSize /= k) < k) {
            return String.format("%.2fM", humanSize);
        }
        humanSize /= k;
        return String.format("%.2fG", humanSize);
    }

    private int payloadSize(String payload) {
        if (payload == null) {
            return 0;
        }
        if (BINARY_DATA.equals(payload)) {
            return 0;
        }
        return payload.getBytes(StandardCharsets.UTF_8).length;
    }

    private String formatElapsed(long elapsedMillis) {
        return elapsedMillis + "ms";
    }

    private void logInfo(String configKey, String format, Object... args) {
        logger.info(String.format(methodTag(configKey) + format, args));
    }

    private String methodTag(String configKey) {
        return "[" + configKey + "] ";
    }

    public static final class LogContext {
        private final ReactiveHttpRequest request;
        private final String configKey;
        private final long startTime = System.currentTimeMillis();
        private ReactiveHttpResponse<?> response;
        private StringBuilder requestMessage;
        private StringBuilder responseMessage;
        private boolean requestLogged;
        private boolean responseLogged;

        public LogContext(ReactiveHttpRequest request, String configKey) {
            this.request = request;
            this.configKey = configKey;
        }

        public ReactiveHttpRequest getRequest() {
            return request;
        }

        public String getConfigKey() {
            return configKey;
        }

        public ReactiveHttpResponse<?> getResponse() {
            return response;
        }

        public void setResponse(ReactiveHttpResponse<?> response) {
            this.response = response;
        }

        public StringBuilder getRequestMessage() {
            return requestMessage;
        }

        public void setRequestMessage(StringBuilder requestMessage) {
            this.requestMessage = requestMessage;
        }

        public StringBuilder getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(StringBuilder responseMessage) {
            this.responseMessage = responseMessage;
        }

        public boolean isRequestLogged() {
            return requestLogged;
        }

        public void setRequestLogged(boolean requestLogged) {
            this.requestLogged = requestLogged;
        }

        public boolean isResponseLogged() {
            return responseLogged;
        }

        public void setResponseLogged(boolean responseLogged) {
            this.responseLogged = responseLogged;
        }

        public long elapsedMillis() {
            return System.currentTimeMillis() - startTime;
        }
    }
}
