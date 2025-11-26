package com.dasmo.drs_api.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class UserDto {
	private String username;
	private String fullName;
	private OfficeDto office;
	private Set<String> roles;
}
