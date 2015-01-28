package com.jinheng.fyp.exceptions;

public class MyRecoverableException extends MyException {

	private static final long serialVersionUID = -7186662057927298545L;

	public MyRecoverableException(String code, String message) {
		super(code, message);
	}
}
