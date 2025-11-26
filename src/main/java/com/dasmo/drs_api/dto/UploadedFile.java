package com.dasmo.drs_api.dto;

import lombok.Data;

@Data
public class UploadedFile {
	private String storedName;
	private String originalName;
	private String contentType;
	private String extension;
	private String filePath;
}
