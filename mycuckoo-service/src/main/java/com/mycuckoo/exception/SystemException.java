package com.mycuckoo.exception;

/**
 * 功能说明: 系统异常
 * 
 * @author rutine
 * @time Sep 23, 2014 11:03:15 AM
 * @version 2.0.0
 */
@SuppressWarnings("serial")
public class SystemException extends Exception {

	public SystemException() {
		super();
	}

	public SystemException(String mesg) {
		super(mesg);
	}

	public SystemException(String mesg, Throwable rootCause) {
		super(mesg);
		rootCause.printStackTrace();
	}
}
