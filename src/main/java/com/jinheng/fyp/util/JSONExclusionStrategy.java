package com.jinheng.fyp.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JSONExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return clazz.getAnnotation(JSONExclusion.class) != null;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(JSONExclusion.class) != null;
	}
}
