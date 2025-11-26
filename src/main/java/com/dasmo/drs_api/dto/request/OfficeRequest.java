package com.dasmo.drs_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OfficeRequest {

	@NotBlank(message = "Office name is required")
	private String name;
	private String code;
	private String department;
}
