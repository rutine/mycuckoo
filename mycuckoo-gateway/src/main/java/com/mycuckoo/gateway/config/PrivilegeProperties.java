package com.mycuckoo.gateway.config;

import com.google.common.collect.Sets;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Set;

@RefreshScope
@ConfigurationProperties(prefix = PrivilegeProperties.PREFIX)
public class PrivilegeProperties {
    public static final String PREFIX = "mycuckoo.config";

    private Set<String> allowUrls = Sets.newHashSet(); //允许访问资源
    private Set<String> sessionUrls = Sets.newHashSet(); //需登录访问资源

    public Set<String> getAllowUrls() {
        return allowUrls;
    }

    public void setAllowUrls(Set<String> allowUrls) {
        this.allowUrls = allowUrls;
    }

    public Set<String> getSessionUrls() {
        return sessionUrls;
    }

    public void setSessionUrls(Set<String> sessionUrls) {
        this.sessionUrls = sessionUrls;
    }
}
