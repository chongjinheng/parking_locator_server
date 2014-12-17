package com.jinheng.fyp.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON Factory serialization and deserialization
 * 
 * @author original author
 */
public class JSONFactory {

	public static Gson create() {
		ExclusionStrategy ex = new JSONExclusionStrategy();
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		return gsonBuilder.disableHtmlEscaping().addSerializationExclusionStrategy(ex).create();
	}

	public static String toJson(final Object dto) {
		return create().toJson(dto);
	}

	@SuppressWarnings("unchecked")
	public static Object fromJSON(final String secret, @SuppressWarnings("rawtypes") final Class clazz) {
		return create().fromJson(secret, clazz);
	}

}
