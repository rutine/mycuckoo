package com.mycuckoo.domain.uum;

import com.mycuckoo.domain.BasicDomain;

import java.time.LocalDateTime;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/6 10:10
 */
public class Captcha extends BasicDomain<String> {
    private String type;
    private String code;
    private Integer status;
    private LocalDateTime expireTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}