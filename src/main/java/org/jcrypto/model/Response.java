package org.jcrypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

public class Response {
	private static final String RESPONSE = "response";
	private static final String ERROR = "error";
	private static final String ERR_DETAIL = "errorDetail";

	@JsonProperty(RESPONSE)
	private final Map<String, Object> responseAttrs = new HashMap<>();

	public static Response errorResponse(Exception ex) {
		return new Response(ImmutableMap.of(ERROR, StringUtils.defaultIfBlank(ex.getMessage(), ""),
				ERR_DETAIL, ExceptionUtils.getStackTrace(ex)));
	}

	public static Response successResponse(Map<String, Object> respData) {
		return new Response(respData);
	}

	private Response(Map<String, Object> responseAttrs) {
		this.responseAttrs.putAll(responseAttrs);
	}

	private void put(String key, Object value) {
		responseAttrs.put(key, value);
	}
}
