package org.bitbucket.shevchenkod.restaurant.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
	// cached mapper - stores reflection information for object types,
	// otherwise even simple Json de-serializations take 20-50 millis
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	static {
		JSON_MAPPER.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
		//JSON_MAPPER.configure(SerializationFeature.USE_ANNOTATIONS, true);
	}

	public static JsonNode parse(String json) {
		try {
			return JSON_MAPPER.readValue(json, JsonNode.class);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T parseAs(Class<T> clazz, String json) {
		try {
			return JSON_MAPPER.readValue(json, clazz);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T parseAs(Class<T> clazz, byte[] json) {
		try {
			return JSON_MAPPER.readValue(json, clazz);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T parseAs(TypeReference typeReference, String json) {
		try {
			return JSON_MAPPER.readValue(json, typeReference);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	public static <T> T parseAs(JavaType javaType, String json) {
		try {
			return JSON_MAPPER.readValue(json, javaType);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonNode toJson(final Object data) {
		try {
			return JSON_MAPPER.valueToTree(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
