package com.mycuckoo.exception;

/**
 * 功能说明: 应用自定义异常
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 11:02:46 AM
 */
public class ApplicationException extends RuntimeException {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 468263110971341016L;

    private int code = 500;
    private String msg;

    public ApplicationException() {
    }

    /**
     * @param mesg
     */
    public ApplicationException(String mesg) {
        this(500, mesg);
    }

    /**
     * @param code
     * @param mesg
     */
    public ApplicationException(int code, String mesg) {
        this(code, mesg, null);
    }

    /**
     * @param thrab
     */
    public ApplicationException(Throwable thrab) {
        this(500, "", thrab);
    }

    /**
     * @param mesg
     * @param thrab
     */
    public ApplicationException(String mesg, Throwable thrab) {
        this(500, mesg, thrab);
    }

    /**
     * @param code
     * @param mesg
     * @param thrab
     */
    public ApplicationException(int code, String mesg, Throwable thrab) {
        super(mesg, thrab);
        this.code = code;
        this.msg = mesg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
