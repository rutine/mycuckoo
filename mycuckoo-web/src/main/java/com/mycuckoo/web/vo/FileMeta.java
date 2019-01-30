package com.mycuckoo.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 功能说明: 文件异步上传封装对象
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 10:53:47 AM
 */
// ignore "bytes" when return json format
@JsonIgnoreProperties({"bytes"})
public class FileMeta {
    private String url;
    private String name;
    private Long size;
    private String type;
    private byte[] bytes;

    // setters & getters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
