package com.jinheng.fyp.exceptions;

public class SessionTimedOutException extends MyException {

	private static final long serialVersionUID = -4922528865996138097L;

	public SessionTimedOutException(String code, String message) {
		super(code, message);
	}
}
