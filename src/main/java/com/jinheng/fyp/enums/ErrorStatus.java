package com.jinheng.fyp.enums;

public enum ErrorStatus {
	// error code between 5000 - 5999 && 8000 - 8999 are recoverable error, it's fine. user can retry

	SESSION_TIMED_OUT("2002", "Session expired.", "error.session-timed-out"),

	ACCESS_DENIED("2003", "Access denied", "error.authorization-error"),

	ERROR_CLIENT_DISCONNECTED("2007", "Error, client disconnected", "error.client-disconnected"),

	ERROR_METHOD_INVOCATION_ERROR("2008", "Method invocation error", "error.method-invocation"),

	UNHANDLED_ERROR("2009", "Service is currently unavailable. Please try again,if the problem persists kindly contact our Merchant Hotline.", "error.unhandled-error"),

	ERROR_SERVICE_NOT_FOUND("2010", "Invalid service name /version", "error.service-not-found"),

	NULL_FIELD_ERROR("8000", "Empty field detected", "error.empty-field"),

	/** SERVICES **/
	VALIDATION_ERROR("8100", "Please check and ensure that both email address and password are correct.", "error.invalid-account"),

	PASSWORD_STYLE_ERROR("8101", "Your password must be 8 to 15 characters.", "error.invalid-style"),

	PASSWORD_INVALID("8107", "Current password is invalid.", "error.password-invalid"),

	EMAIL_STYLE_ERROR("8102", "Please enter a valid email address.", "error.email-format"),

	EMAIL_EXISTS("8103", "This email is registered\nPlease use another email.", "error.email-exists"),

	PASSWORD_REQ_ERROR("8104", "You have used this password before.\n\nPlease enter a different password", "error.pass-req-error"),

	USER_DOES_NOT_EXIST("8105", "This email is not registered\nPlease register before logging in", "error.user-exist"),

	USER_PARKED("8108", "You have already parked your vehicle elsewhere.\n\nProceed to park in this parking lot?", "error.user-parked");

	private final String defaultMessage;
	private final String key;
	private final Object[] param;
	private String code;

	ErrorStatus(final String code, final String defaultMessage, final String key) {
		this.defaultMessage = defaultMessage;
		this.code = code;
		this.key = key;
		param = null;
	}

	ErrorStatus(final String code, final String defaultMessage, final String key, final Object... param) {
		this.defaultMessage = defaultMessage;
		this.key = key;
		this.param = param;
		this.code = code;
	}

	public final String getDefaultMessage() {
		return defaultMessage;
	}

	public final String getKey() {
		return key;
	}

	public final Object[] getParam() {
		return param;
	}

	public final String getCode() {
		return code;
	}
}
