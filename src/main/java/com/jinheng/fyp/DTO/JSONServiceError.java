package com.jinheng.fyp.DTO;

public class JSONServiceError {

	private final String code;// eIndex
	private final String message;

	public JSONServiceError(final String errorCode, final String message) {
		this.code = errorCode;
		this.message = message;
	}

	public final String getCode() {
		return code;
	}

	public final String getMessage() {
		return message;
	}
}
