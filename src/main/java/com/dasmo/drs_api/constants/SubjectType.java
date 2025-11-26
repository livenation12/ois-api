package com.dasmo.drs_api.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SubjectType {

	DOCUMENT("Document");

	private final String displayName;

	SubjectType(String displayName){
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}
}
