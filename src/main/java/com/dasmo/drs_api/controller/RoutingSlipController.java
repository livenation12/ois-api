package com.dasmo.drs_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.dto.request.RoutingSlipRequest;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.service.RoutingSlipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routing-slips")
public class RoutingSlipController {

	private final RoutingSlipService routingService;

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> createRoutingSlip(@ModelAttribute @Validated RoutingSlipRequest request){
		routingService.doCreate(request);
		return ApiResponse.success("Successfully created routing slip", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Object>> getRoutingSlips(){
		return ApiResponse.success("");
	}

}
