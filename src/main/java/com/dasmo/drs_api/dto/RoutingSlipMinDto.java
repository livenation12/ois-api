package com.dasmo.drs_api.dto;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.constants.Urgency;

import lombok.Data;

@Data
public class RoutingSlipMinDto {
	private Long id;

	private String docTin;

	private String title;
	private String subject;
	private String action;

	private String actionRequested;

	private DocumentStatus status;

	private Urgency urgency;
}
