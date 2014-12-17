package com.jinheng.fyp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.exceptions.MyMobileRequestException;

/**
 * Validators to ensure the String is in the correct format
 * 
 * @author chongjinheng
 * @author chengyang
 */
public class Validators {

	/**
	 * Validations for email address format
	 * 
	 * @param inputEmail
	 * @return boolean
	 */
	public static boolean validateEmail(String inputEmail) {
		Pattern pattern;
		Matcher matcher;

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		pattern = Pattern.compile(EMAIL_PATTERN);

		matcher = pattern.matcher(inputEmail);
		return matcher.matches();
	}

	/**
	 * Check password and return true if its same
	 * 
	 * @param password
	 * @param realPass
	 * @param email
	 * @return boolean
	 */
	public static boolean validatePassword(String password, String realPass, String email) {
		if ((Encryptor.hashPassword(password, email).equals(realPass)))
			return true;
		else
			return false;
	}

	public static boolean checkPasswordStyle(String password) {
		Pattern pattern;
		Matcher matcher;

		final String PASSWORD_PATTERN = "^([0-9]+[a-zA-Z]+|[a-zA-Z]+[0-9]+)[0-9a-zA-Z]*$";

		pattern = Pattern.compile(PASSWORD_PATTERN);

		matcher = pattern.matcher(password);
		return matcher.matches();

	}

	/**
	 * Check whether a String is null and trim it.
	 * 
	 * @param stringParam String
	 * @throws MyMobileRequestException
	 * @return String
	 */
	public static String sanityCheck(String stringParam) throws MyMobileRequestException {
		if (stringParam == null) {
			throw new MyMobileRequestException(ErrorStatus.NULL_FIELD_ERROR, ErrorStatus.NULL_FIELD_ERROR.getDefaultMessage());
		}
		stringParam = stringParam.trim();
		if (StringUtils.isEmpty(stringParam)) {
			throw new MyMobileRequestException(ErrorStatus.NULL_FIELD_ERROR, ErrorStatus.NULL_FIELD_ERROR.getDefaultMessage());
		}
		return stringParam;
	}

	/**
	 * Check whether a Long is null
	 * 
	 * @param longParam Long
	 * @throws MyMobileRequestException
	 */
	public static void sanityCheck(Long longParam) throws MyMobileRequestException {
		if (longParam == null) {
			throw new MyMobileRequestException(ErrorStatus.NULL_FIELD_ERROR, ErrorStatus.NULL_FIELD_ERROR.getDefaultMessage());
		}
	}

	/**
	 * Check whether an Integer is null
	 * 
	 * @param intParam Integer
	 * @throws MyMobileRequestException
	 */
	public static void sanityCheck(Integer intParam) throws MyMobileRequestException {
		if (intParam == null) {
			throw new MyMobileRequestException(ErrorStatus.NULL_FIELD_ERROR, ErrorStatus.NULL_FIELD_ERROR.getDefaultMessage());
		}
	}

}
