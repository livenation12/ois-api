package com.dasmo.drs_api.dto;

import lombok.Data;

@Data
public class UserMinDto {
	private Long id;
	private String fullName;
	private String officeName;
	private String officeId;
}
