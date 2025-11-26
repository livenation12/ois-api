package com.dasmo.drs_api.dto;

import java.time.LocalDateTime;

import com.dasmo.drs_api.constants.RoutingAction;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DocumentLogDto {

	private Long id;

	private Long documentId;

	private RoutingAction action;

	private UserMinDto receiver;

	private UserMinDto sender;

	private OfficeMinDto fromOffice;

	private OfficeMinDto toOffice;

	private String additionalRemarks;
	private String remarks;

	@JsonFormat(pattern = "MMM dd yyyy HH:mm:ss a", timezone = "Asia/Manila")
	private LocalDateTime createdAt;

	private RoutingSlipDto routingSlip;

	private UserMinDto createdBy;
}
