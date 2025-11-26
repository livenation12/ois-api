package com.dasmo.drs_api.dto.request;

import com.dasmo.drs_api.constants.TargetType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentActionRequest {

	@NotNull(message="To is required")
	private Long toId;
	private String remarks;
	private String additionalRemarks;
	private TargetType targetType = TargetType.OFFICE;
}
