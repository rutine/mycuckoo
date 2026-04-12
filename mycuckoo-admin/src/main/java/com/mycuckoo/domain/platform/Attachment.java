package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 5.0.0
 * @time Mar 11, 2026 8:59:44 PM
 */
public class Attachment extends BasicDomain<Long> {
    private Integer busiType;
    private Integer busiSubType;
    private String busiId;
    private String fileId;
    private String filePath;
    private String fileName;
    private Long fileSize;
    private String fileType;

    public Integer getBusiType() {
        return busiType;
    }

    public void setBusiType(Integer busiType) {
        this.busiType = busiType;
    }

    public Integer getBusiSubType() {
        return busiSubType;
    }

    public void setBusiSubType(Integer busiSubType) {
        this.busiSubType = busiSubType;
    }

    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
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
