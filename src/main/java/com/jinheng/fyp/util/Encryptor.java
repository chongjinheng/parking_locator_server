package com.jinheng.fyp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

	public static String hashPassword(String password, String email) {
		String salt = email;
		String hash = password + salt;

		for (int i = 0; i < 3000; i++) {
			hash = doSha512(hash + password + salt);
		}

		return hash;
	}

	private static String doSha512(String temp) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(temp.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("sha512 encryption ERROR!");
			return temp;
		}
	}

	public static StringBuilder hidePasswordFromMobile(String jsonFromMobile) {
		StringBuilder newJsonFromMobile = new StringBuilder(jsonFromMobile);
		String targetfield = "\"password\" : \"";
		if (jsonFromMobile.contains(targetfield)) {

			Integer startIndex = jsonFromMobile.indexOf(targetfield) + 14;

			for (int i = startIndex; i < jsonFromMobile.length(); i++) {
				if (jsonFromMobile.charAt(i) == '\"')
					break;
				newJsonFromMobile.setCharAt(i, '*');
			}
		}
		return newJsonFromMobile;
	}

	public static StringBuilder hideSessionKeyFromMobile(String jsonFromMobile) {
		StringBuilder newJsonFromMobile = new StringBuilder(jsonFromMobile);
		String targetfield = "\"sessionKey\" : \"";
		if (jsonFromMobile.contains(targetfield)) {

			Integer startIndex = jsonFromMobile.indexOf(targetfield) + 16;

			for (int i = startIndex; i < jsonFromMobile.length(); i++) {
				if (jsonFromMobile.charAt(i + 2) == '\"')
					break;
				newJsonFromMobile.setCharAt(i, '*');
			}
		}
		return newJsonFromMobile;
	}

	public static StringBuilder hidePassword(String jsonFromMobile) {
		StringBuilder newJsonFromMobile = new StringBuilder(jsonFromMobile);
		String targetfield = "\"password\": \"";
		if (jsonFromMobile.contains(targetfield)) {

			Integer startIndex = jsonFromMobile.indexOf(targetfield) + 13;

			for (int i = startIndex; i < jsonFromMobile.length(); i++) {
				if (jsonFromMobile.charAt(i) == '\"')
					break;
				newJsonFromMobile.setCharAt(i, '*');
			}
		}
		return newJsonFromMobile;
	}

	public static StringBuilder hideSessionKey(String jsonFromMobile) {
		StringBuilder newJsonFromMobile = new StringBuilder(jsonFromMobile);
		String targetfieldSession = "\"sessionKey\": \"";
		if (jsonFromMobile.contains(targetfieldSession)) {

			Integer startIndex = jsonFromMobile.indexOf(targetfieldSession) + 15;

			for (int i = startIndex; i < jsonFromMobile.length(); i++) {
				if (jsonFromMobile.charAt(i + 2) == '\"')
					break;
				newJsonFromMobile.setCharAt(i, '*');
			}
		}
		return newJsonFromMobile;
	}
}
