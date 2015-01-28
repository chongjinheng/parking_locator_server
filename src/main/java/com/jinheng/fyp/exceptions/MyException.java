package com.jinheng.fyp.exceptions;

public class MyException extends Exception {

	private static final long serialVersionUID = 7202280071464908288L;

	/** Error code of the exception. */
	private final String code;

	public MyException(final String code, final String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
