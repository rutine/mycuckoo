package com.mycuckoo.admin.entity.platform;

import com.mycuckoo.admin.entity.BasicEntity;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 5.0.0
 * @time Mar 11, 2026 8:59:44 PM
 */
public class Attachment extends BasicEntity<Long> {
    private Integer bizType;
    private Integer bizSubType;
    private String bizId;
    private String fileId;
    private String filePath;
    private String fileName;
    private Long fileSize;
    private String fileType;

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Integer getBizSubType() {
        return bizSubType;
    }

    public void setBizSubType(Integer bizSubType) {
        this.bizSubType = bizSubType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
