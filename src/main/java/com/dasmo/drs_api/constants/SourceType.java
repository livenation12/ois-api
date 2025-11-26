package com.dasmo.drs_api.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SourceType {
	EXTERNAL("External"), INTERNAL("Internal");

	private final String displayName;

	SourceType(String displayName) {
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}
}
