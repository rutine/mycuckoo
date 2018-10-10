package com.mycuckoo.web.vo;

/**
 * 功能说明: 统一请求头
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 16, 2017 8:54:17 AM
 */
public class AjaxRequest<R> {
    private long timestamp;
    private R data;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }
}
