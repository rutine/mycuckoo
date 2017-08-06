package com.mycuckoo.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 功能说明: 文件异步上传封装对象
 * 
 * @author rutine
 * @time Sep 23, 2014 10:53:47 AM
 * @version 2.0.0
 */
// ignore "bytes" when return json format
@JsonIgnoreProperties({ "bytes" })
public class FileMeta {
	private String url;
	private String thumbnail_url;
	private String name;
	private Long size;
	private String type;
	private String delete_url;
	private String delete_type;
	private byte[] bytes;

	// setters & getters
	public String getUrl() {
		return url;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public String getName() {
		return name;
	}

	public Long getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public String getDelete_url() {
		return delete_url;
	}

	public String getDelete_type() {
		return delete_type;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDelete_url(String delete_url) {
		this.delete_url = delete_url;
	}

	public void setDelete_type(String delete_type) {
		this.delete_type = delete_type;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
