package com.dasmo.drs_api.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dasmo.drs_api.constants.SourceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentRequest {

	@NotBlank
	private String title;

	@NotNull
	private Long sourceId;

	private SourceType sourceType;

	private String description;

	private String urgency;

	private MultipartFile attachment;

	private List<MultipartFile> attachments;

}
