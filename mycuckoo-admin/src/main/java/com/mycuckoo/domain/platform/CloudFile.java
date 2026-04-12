package com.mycuckoo.domain.platform;

import java.time.LocalDateTime;

/**
 * 功能说明: 云文件域对象
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 11, 2026 8:59:44 AM
 */
public class CloudFile {
    private String id; //ID
    private String path;
    private String name;
    private Long size;
    private String type;
    private String creator; //创建人
    private LocalDateTime createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
