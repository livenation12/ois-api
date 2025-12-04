package com.dasmo.drs_api.dto;

import java.time.LocalDateTime;

import com.dasmo.drs_api.constants.ActionTypes;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class DocumentLogMinDto {
	private Long id;
	private Long documentId;
	private ActionTypes action;
	private Object from;
	private Object to;

	private Boolean isActive;
	@JsonFormat(pattern = "MMM dd ha", timezone = "Asia/Manila")
	private LocalDateTime createdAt;

}
