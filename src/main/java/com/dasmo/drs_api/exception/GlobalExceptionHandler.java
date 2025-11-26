package com.dasmo.drs_api.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.dasmo.drs_api.payload.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	ResponseEntity<ApiResponse<String>> handleApiException(ApiException ex) {
		return ResponseEntity
				.status(ex.getStatus())
				.body(ApiResponse.error(ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiResponse<Object>> handleEnumError(MethodArgumentTypeMismatchException ex) {
		if (ex.getRequiredType().isEnum()) {
			return ResponseEntity.badRequest()
					.body(ApiResponse.error("Invalid status value: " + ex.getValue()));
		}
		return ResponseEntity.badRequest()
				.body(ApiResponse.error("Invalid request parameter"));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(FieldValidationException.class)
	public ApiResponse<Map<String, String>> handleFieldValidationException(FieldValidationException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(ex.getField(), ex.getMessage());
		return  ApiResponse.error(errors);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	ApiResponse<String> handleServerException(Exception ex){
		return ApiResponse.error("Server error");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	ApiResponse<String> illegalArgsErrors(IllegalArgumentException ex){
		return ApiResponse.error(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	ApiResponse<String> runtimeErrors(RuntimeException ex){
		return ApiResponse.error(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ApiResponse<Map<String, Object>> validationErrors(MethodArgumentNotValidException ex){
		Map<String, Object> response = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(error -> error.getField(), error -> error.getDefaultMessage()));
		return ApiResponse.error(response);
	}

}
