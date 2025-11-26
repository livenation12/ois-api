package com.dasmo.drs_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class DocumentAttachmentDto extends BaseDto {
	private Long id;
	private Long documentId;
	private String storedName;
	private String filePath;
	private String originalName;
}
