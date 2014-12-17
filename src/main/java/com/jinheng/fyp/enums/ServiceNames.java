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

	/**
	 * service to perform fetch inventory list.
	 */
	GET_INVENTORY_LIST,

	/**
	 * service to manage category.
	 */
	MANAGE_CATEGORY,

	/**
	 * service to manage category item.
	 */
	MANAGE_CATEGORY_ITEM,

	/**
	 * service to manage product.
	 */
	MANAGE_PRODUCT,

	/**
	 * service to manage product.
	 */
	MANAGE_DISCOUNT,

	/**
	 * service to perform updater user profile.
	 */
	UPDATE_USER_PROFILE,

	/**
	 * service to confirm sales.
	 */
	CONFIRM_SALE,

	/**
	 * service to perform send receipt.
	 */
	SEND_RECEIPT,

	/**
	 * service to view sales history
	 */
	SALES_HISTORY,

	/**
	 * service to view sales history details
	 */
	SALES_HISTORY_DETAIL
}
