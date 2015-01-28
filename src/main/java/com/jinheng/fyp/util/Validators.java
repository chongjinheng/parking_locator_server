package com.jinheng.fyp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.exceptions.MyMobileRequestException;

public class Validators {

	public static boolean validateEmail(String inputEmail) {
		Pattern pattern;
		Matcher matcher;

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		pattern = Pattern.compile(EMAIL_PATTERN);

		matcher = pattern.matcher(inputEmail);
		return matcher.matches();
	}

	public static boolean validatePassword(String password, String realPass, String email) {
		if ((Encryptor.hashPassword(password, email).equals(realPass)))
			return true;
		else
			return false;
	}

	public static boolean checkPasswordStyle(String password) {
		Pattern pattern;
		Matcher matcher;

		final String PASSWORD_PATTERN = "^[\\p{L}\\p{N}]{8,15}$";

		pattern = Pattern.compile(PASSWORD_PATTERN);

		matcher = pattern.matcher(password);
		return matcher.matches();

	}

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

	public static void sanityCheck(Long longParam) throws MyMobileRequestException {
		if (longParam == null) {
			throw new MyMobileRequestException(ErrorStatus.NULL_FIELD_ERROR, ErrorStatus.NULL_FIELD_ERROR.getDefaultMessage());
		}
	}

	public static void sanityCheck(Integer intParam) throws MyMobileRequestException {
		if (intParam == null) {
			throw new MyMobileRequestException(ErrorStatus.NULL_FIELD_ERROR, ErrorStatus.NULL_FIELD_ERROR.getDefaultMessage());
		}
	}

}
