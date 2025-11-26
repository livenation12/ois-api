package com.dasmo.drs_api.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoutingAction {
	ENCODED("Encoded"),
	FORWARDED("Forwarded"),
	REVERTED("Reverted"),
	APPROVED("Approved"),
	RECEIVED("Received");

	private final String displayName;

	RoutingAction(String displayName){
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}
}
