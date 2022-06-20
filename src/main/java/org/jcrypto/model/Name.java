package org.jcrypto.model;

public class Name {
	public enum Type {
		STRING, INTEGER, DATETIME
	}
	private final String uiId;
	private final String name;
	private final String label;
	private final Type type;

	public Name(String uiId, String name, String label, Type type) {
		this.uiId = uiId;
		this.name = name;
		this.label = label;
		this.type = type;
	}

	public String getUiId() {
		return uiId;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public Type getType() {
		return type;
	}
}
