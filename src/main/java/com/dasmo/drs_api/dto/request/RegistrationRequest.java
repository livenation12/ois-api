package com.dasmo.drs_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequest {
	@NotBlank(message = "Username is required")
	private String username;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

	//	@Email(message = "Invalid email format")
	//	@NotBlank(message = "Email is required")
	//	private String email;
	//
	//	@NotBlank(message = "First name is required")
	//	private String firstName;
	//
	//	private String middleName;
	//	@NotBlank(message = "Last name is required")
	//	private String lastName;
}
