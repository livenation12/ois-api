package com.dasmo.drs_api.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TargetType {
	OFFICE("Office"),
	USER("User");

	private final String displayName;

	TargetType(String displayName) {
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}
}
