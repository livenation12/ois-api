package com.dasmo.drs_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.dto.DocumentDto;
import com.dasmo.drs_api.dto.DocumentFullDto;
import com.dasmo.drs_api.dto.RoutingSlipDto;
import com.dasmo.drs_api.dto.request.DocumentActionRequest;
import com.dasmo.drs_api.dto.request.DocumentRequest;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.service.DocumentActionService;
import com.dasmo.drs_api.service.DocumentService;
import com.dasmo.drs_api.service.RoutingSlipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

	private final DocumentService documentService;
	private final DocumentActionService docActionService;
	private final RoutingSlipService routingService;

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> doCreateDocument(@ModelAttribute @Validated DocumentRequest request) {
		documentService.doCreate(request);
		return ApiResponse.success("Added successfully");
	}

	@PostMapping("/{id}/forward")
	public ResponseEntity<ApiResponse<Object>> doForwardDocument(@PathVariable Long id,
			@RequestBody @Validated DocumentActionRequest request) {
		docActionService.doForwardDocument(id, request);
		return ApiResponse.success("Document forwarded successfully");
	}

	@PostMapping("/{id}/receive")
	public ResponseEntity<ApiResponse<Object>> doReceiveDocument(@PathVariable Long id){
		docActionService.doReceiveDocument(id);
		return ApiResponse.success("Document successfuly received");
	}

	@PostMapping("/{id}/office-receive")
	public ResponseEntity<ApiResponse<Object>> doReceiveOfficeDocument(@PathVariable Long id){
		docActionService.doReceiveOfficeDocument(id);
		return ApiResponse.success("Office document successfuly received");
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Object>> getAllDocuments(
			@RequestParam(name = "status", required = false) DocumentStatus status) {
		if (status != null) {
			List<DocumentDto> filteredByStatusDocuments = documentService.fetchByFilteredStatus(status);
			return ApiResponse.success(filteredByStatusDocuments);
		}
		List<DocumentDto> documentList = documentService.fetchAll();
		return ApiResponse.success(documentList);
	}

	@GetMapping("/principal")
	public ResponseEntity<ApiResponse<Object>> getCreatedDocuments() {
		List<DocumentDto> documents = documentService.fetchCreatedDocuments();
		return ApiResponse.success(documents);
	}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> getDocumentDetails(@PathVariable Long id) {
		DocumentDto document = documentService.fetchDocumentDetails(id);
		return ApiResponse.success(document);
	}

	@GetMapping("/{id}/full-details")
	public ResponseEntity<ApiResponse<Object>> getDocumentFullDetails(@PathVariable Long id) {
		DocumentFullDto document = documentService.fetchDocumentFullDetails(id);
		return ApiResponse.success(document);
	}

	@GetMapping("/{id}/routing-slips")
	public ResponseEntity<ApiResponse<Object>> getDocumentRoutings(@PathVariable Long id){
		List<RoutingSlipDto> routingSlip = routingService.fetchByDocumentId(id);
		return ApiResponse.success(routingSlip);
	}

	@GetMapping("/encoded")
	public ResponseEntity<ApiResponse<Object>> getEncodedDocuments() {
		List<DocumentDto> documents = documentService.fetchEncodedDocuments();
		return ApiResponse.success(documents);
	}

	@GetMapping("/forwarded")
	public ResponseEntity<ApiResponse<Object>> getForwardedDocuments() {
		List<DocumentDto> documents = documentService.fetchForwardedDocuments();
		return ApiResponse.success(documents);
	}

	@GetMapping("/office-forwarded")
	public ResponseEntity<ApiResponse<Object>> getOfficeForwardedDocuments() {
		List<DocumentDto> documents = documentService.fetchOfficeForwardedDocuments();
		return ApiResponse.success(documents);
	}
	@GetMapping("/office-pendings")
	public ResponseEntity<ApiResponse<Object>> getOfficePendingDocuments() {
		List<DocumentDto> documents = documentService.fetchOfficePendingDocuments();
		return ApiResponse.success(documents);
	}

	@GetMapping("/office-received")
	public ResponseEntity<ApiResponse<Object>> getOfficeReceivedDocuments() {
		List<DocumentDto> documents = documentService.fetchOfficeReceivedDocuments();
		return ApiResponse.success(documents);
	}

	@GetMapping("/user-forwarded")
	public ResponseEntity<ApiResponse<Object>> getUserForwardedDocuments() {
		List<DocumentDto> documents = documentService.fetchUserForwardedDocuments();
		return ApiResponse.success(documents);
	}

	@GetMapping("/user-pendings")
	public ResponseEntity<ApiResponse<Object>> getUserPendingDocuments() {
		List<DocumentDto> documents = documentService.fetchUserPendingDocuments();
		return ApiResponse.success(documents);
	}

	@GetMapping("/user-received")
	public ResponseEntity<ApiResponse<Object>> getUserReceivedDocuments() {
		List<DocumentDto> documents = documentService.fetchUserReceivedDocuments();
		return ApiResponse.success(documents);
	}

}