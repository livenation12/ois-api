package com.dasmo.drs_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.dto.UserDto;
import com.dasmo.drs_api.dto.request.LoginRequest;
import com.dasmo.drs_api.dto.request.RegistrationRequest;
import com.dasmo.drs_api.model.UserPrincipal;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.security.JwtTokenUtil;
import com.dasmo.drs_api.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final JwtTokenUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Object>> authenticate(@RequestBody LoginRequest request, HttpServletResponse response) {

		UserPrincipal principal = authService.authenticate(request);
		String token = jwtUtil.generateToken(principal.getUsername());

		Cookie cookie = new Cookie("jwt", token);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(24 * 60 * 60);

		response.addCookie(cookie);

		return ApiResponse.success("Login successful");
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<Object>> fetchUser(){
		UserDto principal = authService.fetchCurrentUser();
		return ApiResponse.success(principal);
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Object>> logout(HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		Cookie cookie = new Cookie("jwt", "");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // expire immediately
		response.addCookie(cookie);
		return ApiResponse.success("Logged out successfully");
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegistrationRequest request){
		authService.register(request);
		return ApiResponse.success("Successfully registered", HttpStatus.CREATED);
	}

}
