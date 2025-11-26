package com.dasmo.drs_api.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {

	WARNING("Warning")
	, INFO("Info")
	, ERROR("Error")
	, SUCCESS("Success");

	private final String displayName;

	NotificationType(String displayName){
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}
}
