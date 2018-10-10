package com.mycuckoo.web.vo;

/**
 * 功能说明: 统一响应头
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 16, 2017 9:02:35 AM
 */
public class AjaxResponse<R> {
    private int code;
    private String message;
    private R data;

    public static <R> AjaxResponse<R> create(R data) {
        AjaxResponse<R> response = new AjaxResponse<>();
        response.setData(data);
        return response;
    }

    public static <R> AjaxResponse<R> create(int code, String message) {
        AjaxResponse<R> response = new AjaxResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }
}
