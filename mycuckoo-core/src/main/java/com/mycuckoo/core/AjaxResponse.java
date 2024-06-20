package com.mycuckoo.core;

/**
 * 功能说明: 统一响应头
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 16, 2017 9:02:35 AM
 */
public class AjaxResponse<R> {
    private int code;
    private String msg;
    private R data;

    public static <R> AjaxResponse<R> create(R data) {
        AjaxResponse<R> response = new AjaxResponse<>();
        response.setData(data);
        return response;
    }

    public static <R> AjaxResponse<R> create(int code, String msg) {
        AjaxResponse<R> response = new AjaxResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static <R> AjaxResponse<R> success(String msg) {
        AjaxResponse<R> response = new AjaxResponse<>();
        response.setMsg(msg);
        return response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }
}
