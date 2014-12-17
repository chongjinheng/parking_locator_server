package com.jinheng.fyp.exceptions;

/**
 * Base exception class, that can be used across all SoftSpace projects. POS version of SSPaymentException
 * 
 * @author ChristopherChak
 * @author azliabdullah
 */
public class MyException extends Exception {

	private static final long serialVersionUID = 7202280071464908288L;

	/** Error code of the exception. */
	private final String code;

	/**
	 * Default constructor. Message can be retrieved by calling <code>getMessage()</code>.
	 * 
	 * @param code the error code of the exception
	 * @param message the detailed message of the exception
	 */
	public MyException(final String code, final String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
