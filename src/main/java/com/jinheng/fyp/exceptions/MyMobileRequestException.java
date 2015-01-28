package com.jinheng.fyp.exceptions;

import com.jinheng.fyp.enums.ErrorStatus;

public class MyMobileRequestException extends MyException {

	private static final long serialVersionUID = 1184800589758888L;
	private final ErrorStatus errorStatus;

	public MyMobileRequestException(final ErrorStatus errorStatus, final String msg) {
		super(errorStatus.getCode(), msg);
		this.errorStatus = errorStatus;
	}

	public final ErrorStatus getErrorStatus() {
		return errorStatus;
	}
}
