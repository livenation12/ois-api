package com.dasmo.drs_api.dto;

import java.util.List;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.constants.SourceType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto extends BaseDto {
	private Long id;
	private String documentCode;
	private SourceType sourceType;
	private String title;
	private DocumentStatus status;
	private DocumentLogMinDto activeLog;
	private String description;
	private String sourceName;
	private List<DocumentAttachmentDto> attachments;
}
