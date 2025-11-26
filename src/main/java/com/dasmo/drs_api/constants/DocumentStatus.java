package com.dasmo.drs_api.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentStatus {
	PENDING("Pending"), TO_REVIEW("To review"), DRAFT("Draft"), APPROVED("Approved"), REJECTED("Rejected"),
	COMPLETED("Completed"), RECEIVED("Received"), FORWARDED("Forwarded");

	private final String displayName;

	DocumentStatus(String displayName){
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}

}
