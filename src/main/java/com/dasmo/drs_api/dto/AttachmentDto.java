package com.dasmo.drs_api.dto;

import lombok.Data;

@Data
public class AttachmentDto {
	private Long id;
	private String storedName;
	private String filePath;
	private String originalName;
}
