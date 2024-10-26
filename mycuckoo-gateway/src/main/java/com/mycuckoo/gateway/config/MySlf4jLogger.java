package com.mycuckoo.gateway.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @see feign.slf4j.Slf4jLogger
 * @author rutine
 * @date 2024/8/22 11:02
 */
public class MySlf4jLogger extends Logger {
    private final org.slf4j.Logger logger;

    private FeignLoggerProperties loggerProperties;


    public MySlf4jLogger(FeignLoggerProperties properties) {
        this(MySlf4jLogger.class, properties);
    }

    public MySlf4jLogger(Class<?> clazz, FeignLoggerProperties properties) {
        this(LoggerFactory.getLogger(clazz), properties);
    }

    public MySlf4jLogger(String name, FeignLoggerProperties properties) {
        this(LoggerFactory.getLogger(name), properties);
    }

    MySlf4jLogger(org.slf4j.Logger logger, FeignLoggerProperties properties) {
        this.logger = logger;
        this.loggerProperties = properties;
    }


    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (loggerProperties.isEnabled()) {
            StringBuilder msg = new StringBuilder();
            msg.append(request.httpMethod().name()).append(" ");
            msg.append(request.url());

            if (loggerProperties.isIncludeHeaders()) {
                msg.append(", headers=").append(request.headers());
            }

            if (loggerProperties.isIncludePayload() && request.body() != null) {
                String bodyText = request.charset() != null ? new String(request.body(), request.charset()) : null;
                msg.append(", size=").append(formatContentSize(request.length()));
                msg.append(", payload=").append(bodyText != null ? bodyText : "Binary data");
            }

            this.log2(configKey, "---> %s", msg.toString());
            return;
        }

        if (this.logger.isDebugEnabled()) {
            super.logRequest(configKey, logLevel, request);
        }
    }

    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        if (loggerProperties.isEnabled()) {
            if (loggerProperties.isIncludePayload()) {// && isPlainText(response.headers().get("content-type"))) {
                StringBuilder msg = new StringBuilder();
                if (response.body() != null ) {
                    byte[] buf = Util.toByteArray(response.body().asInputStream());
                    if (buf.length > 0) {
                        int length = Math.min(buf.length, loggerProperties.getMaxPayloadSize());
                        msg.append("size=").append(formatContentSize(buf.length));
                        msg.append(", payload=").append(decodeOrDefault(buf, length, StandardCharsets.UTF_8, "Binary data"));
                        this.log2(configKey, "<--- %s", msg.toString());
                    }

                    return response.toBuilder().body(buf).build();
                }
            }
            return response;
        }

        return this.logger.isDebugEnabled() ? super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime) : response;
    }

    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format(methodTag(configKey) + format, args));
        }
    }

    private void log2(String configKey, String format, Object... args) {
        this.logger.info(String.format(methodTag(configKey) + format, args));
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
    private boolean isPlainText(Collection<String> contentTypes) {
        if (contentTypes == null) {
            return false;
        }
        for (String contentType : contentTypes) {
            if (this.isPlainText(contentType)) {
                return true;
            }
        }

        return false;
    }
    private boolean isPlainText(String contentType) {
        return contentType != null && (contentType.contains(MediaType.APPLICATION_JSON_VALUE) || contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
    }
    private String decodeOrDefault(byte[] data, int length, Charset charset, String defaultValue) {
        if (data == null) {
            return defaultValue;
        } else {
            try {
                return charset.newDecoder().decode(ByteBuffer.wrap(data, 0, length)).toString();
            } catch (CharacterCodingException var4) {
                return defaultValue;
            }
        }
    }
}

