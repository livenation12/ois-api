package com.dasmo.drs_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.dto.DocumentLogDto;
import com.dasmo.drs_api.dto.DocumentLogMinDto;
import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.mapper.DocumentLogMapper;
import com.dasmo.drs_api.model.DocumentLog;
import com.dasmo.drs_api.repo.DocumentLogRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentLogService {

	private final DocumentLogRepo docLogRepo;
	private final DocumentLogMapper docLogMapper;
	private final DocumentService docService;

	public DocumentLogDto fetchById(Long id) {
		DocumentLog docAction = docLogRepo.findById(id)
				.orElseThrow(() -> new ApiException("Document log not found", HttpStatus.NOT_FOUND));
		return docLogMapper.toDto(docAction);
	}

	public List<DocumentLogMinDto> fetchLogsOfDocument(Long documentId){
		List<DocumentLog> docLogs = docLogRepo.findByDocumentIdOrderByCreatedAtDesc(documentId);
		return docLogMapper.toMinDtoList(docLogs);
	}

}
