package com.dasmo.drs_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.dto.UserMinDto;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//temporary
	@GetMapping
	public ResponseEntity<ApiResponse<Object>> getAllUsersExceptPrincipal(){
		List<UserMinDto> users = userService.fetchAllExceptPrincipal();
		return ApiResponse.success(users);
	}

}
