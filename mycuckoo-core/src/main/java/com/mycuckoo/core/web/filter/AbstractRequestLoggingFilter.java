package com.mycuckoo.core.web.filter;

import com.mycuckoo.core.util.IdGenerator;
import com.mycuckoo.core.util.web.InetUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.function.Predicate;

/**
 * @author rutine
 * @time May 1, 2024 8:55:26 AM
 */
public abstract class AbstractRequestLoggingFilter extends OncePerRequestFilter {
    public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";
    public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";
    public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After response [";
    public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";
    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;

    private boolean includeQueryString = false;
    private boolean includeClientInfo = false;
    private boolean includeHeaders = false;
    private boolean includePayload = false;

    @Nullable
    private Predicate<String> headerPredicate;

    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;
    private String beforeMessagePrefix = DEFAULT_BEFORE_MESSAGE_PREFIX;
    private String beforeMessageSuffix = DEFAULT_BEFORE_MESSAGE_SUFFIX;
    private String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;
    private String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;


    /**
     * Set whether the query string should be included in the log message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includeQueryString" in the filter definition in {@code web.xml}.
     */
    public void setIncludeQueryString(boolean includeQueryString) {
        this.includeQueryString = includeQueryString;
    }

    /**
     * Return whether the query string should be included in the log message.
     */
    protected boolean isIncludeQueryString() {
        return this.includeQueryString;
    }

    /**
     * Set whether the client address and session id should be included in the
     * log message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includeClientInfo" in the filter definition in {@code web.xml}.
     */
    public void setIncludeClientInfo(boolean includeClientInfo) {
        this.includeClientInfo = includeClientInfo;
    }

    /**
     * Return whether the client address and session id should be included in the
     * log message.
     */
    protected boolean isIncludeClientInfo() {
        return this.includeClientInfo;
    }

    /**
     * Set whether the request headers should be included in the log message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includeHeaders" in the filter definition in {@code web.xml}.
     * @since 4.3
     */
    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    /**
     * Return whether the request headers should be included in the log message.
     * @since 4.3
     */
    protected boolean isIncludeHeaders() {
        return this.includeHeaders;
    }

    /**
     * Set whether the request payload (body) should be included in the log message.
     * <p>Should be configured using an {@code <init-param>} for parameter name
     * "includePayload" in the filter definition in {@code web.xml}.
     * @since 3.0
     */
    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }

    /**
     * Return whether the request payload (body) should be included in the log message.
     * @since 3.0
     */
    protected boolean isIncludePayload() {
        return this.includePayload;
    }

    /**
     * Configure a predicate for selecting which headers should be logged if
     * {@link #setIncludeHeaders(boolean)} is set to {@code true}.
     * <p>By default this is not set in which case all headers are logged.
     * @param headerPredicate the predicate to use
     * @since 5.2
     */
    public void setHeaderPredicate(@Nullable Predicate<String> headerPredicate) {
        this.headerPredicate = headerPredicate;
    }

    /**
     * The configured {@link #setHeaderPredicate(Predicate) headerPredicate}.
     * @since 5.2
     */
    @Nullable
    protected Predicate<String> getHeaderPredicate() {
        return this.headerPredicate;
    }

    /**
     * Set the maximum length of the payload body to be included in the log message.
     * Default is 50 characters.
     * @since 3.0
     */
    public void setMaxPayloadLength(int maxPayloadLength) {
        Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
        this.maxPayloadLength = maxPayloadLength;
    }

    protected int getMaxPayloadLength() {
        return this.maxPayloadLength;
    }

    public void setBeforeMessagePrefix(String beforeMessagePrefix) {
        this.beforeMessagePrefix = beforeMessagePrefix;
    }

    public void setBeforeMessageSuffix(String beforeMessageSuffix) {
        this.beforeMessageSuffix = beforeMessageSuffix;
    }

    public void setAfterMessagePrefix(String afterMessagePrefix) {
        this.afterMessagePrefix = afterMessagePrefix;
    }

    public void setAfterMessageSuffix(String afterMessageSuffix) {
        this.afterMessageSuffix = afterMessageSuffix;
    }

    protected boolean shouldLog(HttpServletRequest request) {
        return true;
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = request.getHeader("X-Mycuckoo-RequestId");
        MDC.put("traceId", StringUtils.hasText(requestId) ? requestId : IdGenerator.uuid());

        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        boolean shouldLog = shouldLog(requestToUse);
        if (shouldLog && isFirstRequest) {
            beforeRequest(requestToUse, getBeforeMessage(requestToUse));
        }
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        }
        finally {
            if (shouldLog && !isAsyncStarted(requestToUse)) {
                afterResponse(responseToUse, getAfterMessage(responseToUse));
            }

            ((ContentCachingResponseWrapper)responseToUse).copyBodyToResponse();
            MDC.remove("traceId");
        }
    }

    private String getBeforeMessage(HttpServletRequest request) {
        return createMessage(request, this.beforeMessagePrefix, this.beforeMessageSuffix);
    }

    private String getAfterMessage(HttpServletResponse response) {
        StringBuilder msg = new StringBuilder();
        if (isIncludePayload() && isPlainText(response.getContentType())) {
            msg.append(this.afterMessagePrefix);
            ContentCachingResponseWrapper wrapper = getNativeResponse(response, ContentCachingResponseWrapper.class);
            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    int length = Math.min(buf.length, getMaxPayloadLength());
                    msg.append("size=").append(getHumanContentSize(wrapper.getContentSize()));
                    msg.append(", payload=").append(new String(buf, 0, length, StandardCharsets.UTF_8));
                }
            }
            msg.append(this.afterMessageSuffix);
        }

        return msg.toString();
    }
    private String getHumanContentSize(int size) {
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

    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        StringBuilder msg = new StringBuilder();
        msg.append(prefix);
        msg.append(request.getMethod()).append(" ");
        msg.append(request.getRequestURI());

        if (isIncludeQueryString()) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }

        if (isIncludeClientInfo()) {
            String client = InetUtils.getIp(request);
            if (StringUtils.hasLength(client)) {
                msg.append(", client=").append(client);
            }

//            //TODO 暂不支持获取token
//            Cookie token = WebUtils.getCookie(request, RequestConstants.SESSION);
//            if (token != null) {
//                msg.append(", token=").append(token.getValue());
//            } else if(request.getHeader(RequestConstants.SESSION) != null) {
//                msg.append(", token=").append(request.getHeader(RequestConstants.SESSION));
//            }
        }

        if (isIncludeHeaders()) {
            HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
            if (getHeaderPredicate() != null) {
                Enumeration<String> names = request.getHeaderNames();
                while (names.hasMoreElements()) {
                    String header = names.nextElement();
                    if (!getHeaderPredicate().test(header)) {
                        headers.set(header, "masked");
                    }
                }
            }
            msg.append(", headers=").append(headers);
        }

        if (isIncludePayload() && isPlainText(request.getContentType())) {
            String payload = getMessagePayload(request);
            if (payload != null) {
                msg.append(", size=").append(getHumanContentSize(request.getContentLength()));
                msg.append(", payload=").append(payload);
            }
        }

        msg.append(suffix);
        return msg.toString();
    }
    private boolean isPlainText(String contentType) {
        return contentType != null && (contentType.contains(MediaType.APPLICATION_JSON_VALUE) || contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
    }

    @Nullable
    protected String getMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, getMaxPayloadLength());
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding()).replaceAll("[\n]", "");
                }
                catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }

        return null;
    }


    protected abstract void beforeRequest(HttpServletRequest request, String message);

    protected abstract void afterResponse(HttpServletResponse response, String message);


    private <T> T getNativeRequest(ServletRequest request, @Nullable Class<T> requiredType) {
        if (requiredType != null) {
            if (requiredType.isInstance(request)) {
                return (T) request;
            }
            else if (request instanceof ServletRequestWrapper) {
                return getNativeRequest(((ServletRequestWrapper) request).getRequest(), requiredType);
            }
        }
        return null;
    }

    private <T> T getNativeResponse(ServletResponse response, @Nullable Class<T> requiredType) {
        if (requiredType != null) {
            if (requiredType.isInstance(response)) {
                return (T) response;
            }
            else if (response instanceof ServletResponseWrapper) {
                return getNativeResponse(((ServletResponseWrapper) response).getResponse(), requiredType);
            }
        }
        return null;
    }
}
