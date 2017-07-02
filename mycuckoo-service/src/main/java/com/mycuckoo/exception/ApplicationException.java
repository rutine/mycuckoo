package com.mycuckoo.exception;

/**
 * 功能说明: 应用自定义异常
 * 
 * @author rutine
 * @time Sep 23, 2014 11:02:46 AM
 * @version 2.0.0
 */
public class ApplicationException extends Exception {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 468263110971341016L;

	public ApplicationException() {
	}

	/**
	 * @param mesg
	 */
	public ApplicationException(String mesg) {
		super(mesg);
	}

	/**
	 * @param thrab
	 */
	public ApplicationException(Throwable thrab) {
		super(thrab);
	}

	/**
	 * @param mesg
	 * @param thrab
	 */
	public ApplicationException(String mesg, Throwable thrab) {
		super(mesg, thrab);
	}
}
