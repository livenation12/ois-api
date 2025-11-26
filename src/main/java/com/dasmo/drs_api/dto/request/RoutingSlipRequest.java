package com.dasmo.drs_api.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.dasmo.drs_api.constants.Urgency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoutingSlipRequest {

	@NotNull(message = "Document is required")
	private Long documentId;

	@NotBlank(message = "Subject is required")
	private String subject;

	private String action;
	private String actionRequested;

	@NotNull(message = "Urgency is required")
	private Urgency urgency;

	@NotNull(message = "Attachment is required")
	private MultipartFile attachment;
}
