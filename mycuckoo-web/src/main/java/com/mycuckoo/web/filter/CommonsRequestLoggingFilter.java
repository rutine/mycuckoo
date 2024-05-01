package com.mycuckoo.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author rutine
 * @time May 1, 2024 8:55:26 AM
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 */
@Order(1)
public class CommonsRequestLoggingFilter extends AbstractRequestLoggingFilter {
    private static Logger logger = LoggerFactory.getLogger(CommonsRequestLoggingFilter.class);


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterResponse(HttpServletResponse response, String message) {
        logger.info(message);
    }

    @Override
    public boolean isIncludeQueryString() {
        return true;
    }

    @Override
    public boolean isIncludeClientInfo() {
        return false;
    }

    @Override
    public boolean isIncludeHeaders() {
        return false;
    }

    @Override
    public boolean isIncludePayload() {
        return false;
    }

    @Override
    public int getMaxPayloadLength() {
        return 4096;
    }
}
