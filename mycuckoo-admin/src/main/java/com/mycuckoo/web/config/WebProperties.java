package com.mycuckoo.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.mycuckoo.web.config.WebProperties.PREFIX;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/8/23 21:40
 */
@ConfigurationProperties(PREFIX)
public class WebProperties {
    protected static final String PREFIX = "mycuckoo.web";

    private String host;
    private String uploadPath;
    private List<String> allowPaths;
    private List<String> sessionPaths;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }

    public List<String> getSessionPaths() {
        return sessionPaths;
    }

    public void setSessionPaths(List<String> sessionPaths) {
        this.sessionPaths = sessionPaths;
    }
}
