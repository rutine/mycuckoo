package com.mycuckoo.core.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rutine
 * @date 2024/8/22 11:06
 */
@ConfigurationProperties(prefix = FeignLoggerProperties.PREFIX)
public class FeignLoggerProperties {
    protected final static String PREFIX = "mycuckoo.logger.feign";

    private boolean enabled;
    private boolean includeHeaders;
    private boolean includePayload;
    private int maxPayloadSize = 1024;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIncludeHeaders() {
        return includeHeaders;
    }

    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    public boolean isIncludePayload() {
        return includePayload;
    }

    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }

    public int getMaxPayloadSize() {
        return maxPayloadSize;
    }

    public void setMaxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }
}
