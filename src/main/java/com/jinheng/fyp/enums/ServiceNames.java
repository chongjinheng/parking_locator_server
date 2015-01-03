package com.jinheng.fyp.enums;

/**
 * Service names that are used by mobile clients to call the corresponding back-end service.
 * 
 * @author original author
 */
public enum ServiceNames {

	/**
	 * service to perform new Store and User registration
	 */
	SIGN_UP,

	/**
	 * service to perform login existing user
	 */
	LOGIN,

	/**
	 * service to perform logout user.
	 */
	LOGOUT,

	/**
	 * service to send new temporary password to user.
	 */
	FORGOT_PASSWORD,

	/**
	 * service to perform change password.
	 */
	CHANGE_PASSWORD,

	FB_LOGIN,

	GET_PARKING_LOTS;

}