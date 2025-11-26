package com.dasmo.drs_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.constants.SourceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(content = Include.NON_NULL)
public class DocumentFullDto {
	private Long id;

	private String documentCode;

	private SourceType sourceType;

	private String title;

	private String description;

	private OfficeMinDto officeSource;

	private DocumentStatus status;

	private UserDto createdBy;

	@JsonFormat(pattern = "MMM dd yyyy HH:mm:ss a", timezone = "Asia/Manila")
	private LocalDateTime createdAt;

	private List<AttachmentDto> attachments;

}
