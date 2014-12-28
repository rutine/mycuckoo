package com.mycuckoo.domain.vo;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;

/**
 * 功能说明: 异步消息
 * 
 * @author rutine
 * @time Sep 23, 2014 10:53:10 AM
 * @version 2.0.0
 */
public class AjaxResult {

	private boolean status;
	private short code;
	private String msg;
	private Map<String, Object> data = Maps.newHashMap();
	
	public AjaxResult() {
		
	}
	
	public AjaxResult(boolean status, String msg) {
		this(status, (short) 200, msg);
	}
	
	public AjaxResult(boolean status, short code, String msg) {
		this.status = status;
		this.code = code;
		this.msg = msg;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public short getCode() {
		return code;
	}

	public void setCode(short code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
