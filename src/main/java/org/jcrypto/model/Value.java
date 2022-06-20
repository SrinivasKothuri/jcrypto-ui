package org.jcrypto.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Date;
import java.util.Objects;

public class Value {
	private final Object value;
	private final boolean hasValue;

	public Value(Object value) {
		this.value = value;
		this.hasValue = (this.value != null);
	}

	public Number asNumber(Number def) {
		if (this.value instanceof Number)
			return (Number) this.value;

		if (!NumberUtils.isCreatable(this.asString()))
			return def;

		return NumberUtils.createNumber(this.asString());
	}

	public Number asNumber() {
		return asNumber(-1); // avoid NPEs
	}

	public String asString() {
		return asString(null);
	}

	public String asString(String def) {
		return Objects.toString(this.value, def);
	}

	public Date asDate() {
		String val = asString();
		if (StringUtils.isBlank(val))
			new Date();
		return new Date(asNumber().longValue());
	}
}
