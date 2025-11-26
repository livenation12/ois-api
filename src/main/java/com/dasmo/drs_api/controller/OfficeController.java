package com.dasmo.drs_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.dto.OfficeDto;
import com.dasmo.drs_api.dto.request.OfficeRequest;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.service.OfficeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
public class OfficeController {

	private final OfficeService officeService;

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> createOffice(@RequestBody OfficeRequest request){
		officeService.create(request);
		return ApiResponse.success("Office successfully created", HttpStatus.CREATED);
	}

	@GetMapping("/except-principal-office")
	public ResponseEntity<ApiResponse<List<OfficeDto>>> getOfficesExceptPrincipalOffice(){
		return ApiResponse.success(officeService.fetchAllExceptPrincipalOffice());
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<OfficeDto>>> officeList(){
		return ApiResponse.success(officeService.listAll());
	}
}
