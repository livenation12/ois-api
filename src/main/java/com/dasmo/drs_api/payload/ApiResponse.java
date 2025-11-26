package com.dasmo.drs_api.payload;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	public static <T> ApiResponse<T> error(String message) {
		return new ApiResponse<>(false, message, null);
	}
	public static <T> ApiResponse<T> error(T data) {
		return new ApiResponse<>(false, null, data);
	}
	public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status) {
		return new ResponseEntity<>(new ApiResponse<>(true, null, null), status);
	}
	public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
		return new ResponseEntity<>(new ApiResponse<>(true, message, null), HttpStatus.OK);
	}
	public static <T> ResponseEntity<ApiResponse<T>> success(String message,HttpStatus status) {
		return new ResponseEntity<>(new ApiResponse<>(true, message, null), status);
	}
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
		return ResponseEntity.ok(new ApiResponse<>(true, null, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status) {
		return new ResponseEntity<>(new ApiResponse<>(true, null, data), status);
	}

	private boolean success;

	private String message;

	private T data;

	private LocalDateTime timestamp;

	public ApiResponse() {
		this.timestamp = LocalDateTime.now();
	}

	public ApiResponse(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}




}
