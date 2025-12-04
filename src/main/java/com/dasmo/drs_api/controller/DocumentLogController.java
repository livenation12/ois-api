package com.dasmo.drs_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.dto.DocumentLogDto;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.service.DocumentLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents/logs")
@RequiredArgsConstructor
public class DocumentLogController {

	private final DocumentLogService docLogService;

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> getDocumentLog(@PathVariable Long id) {
		DocumentLogDto docLog = docLogService.fetchById(id);
		return ApiResponse.success(docLog);
	}

}
