package org.jcrypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JCryptoServiceUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static JsonNode parseInputStream(InputStream inputStream) throws IOException {
		return MAPPER.readTree(inputStream);
	}

	public static Object parseStringToObject(String jsonStr, Class klass) throws IOException {
		return MAPPER.readValue(jsonStr, klass);
	}

	public static String serialize(Object obj) throws JsonProcessingException {
		return MAPPER.writeValueAsString(obj);
	}
}
