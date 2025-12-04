package com.dasmo.drs_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.constants.ActionTypes;
import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.constants.EntityType;
import com.dasmo.drs_api.constants.SourceType;
import com.dasmo.drs_api.dto.DocumentDto;
import com.dasmo.drs_api.dto.DocumentFullDto;
import com.dasmo.drs_api.dto.UploadedFile;
import com.dasmo.drs_api.dto.request.DocumentRequest;
import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.exception.FieldValidationException;
import com.dasmo.drs_api.mapper.DocumentAttachmentMapper;
import com.dasmo.drs_api.mapper.DocumentLogMapper;
import com.dasmo.drs_api.mapper.DocumentMapper;
import com.dasmo.drs_api.model.Document;
import com.dasmo.drs_api.model.DocumentAttachment;
import com.dasmo.drs_api.model.DocumentLog;
import com.dasmo.drs_api.model.Office;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.repo.DocumentAttachmentRepo;
import com.dasmo.drs_api.repo.DocumentLogRepo;
import com.dasmo.drs_api.repo.DocumentRepo;
import com.dasmo.drs_api.repo.OfficeRepo;
import com.dasmo.drs_api.repo.RoutingSlipRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final String attachmentSubFolder = "/documents";

	private final DocumentRepo documentRepo;
	private final OfficeRepo officeRepo;
	private final DocumentAttachmentRepo docAttachmentRepo;
	private final DocumentLogRepo docActionRepo;
	private final SequenceService sequenceService;
	private final FileStorageService fileStorageService;
	private final RoutingSlipRepo routingRepo;
	private final DocumentMapper docMapper;
	private final DocumentAttachmentMapper docAttachMapper;
	private final UserService userService;
	private final DocumentLogMapper docLogMapper;

	@PreAuthorize("hasRole('ENCODER')")
	@Transactional
	public void doCreate(DocumentRequest request) {
		User principal = PrincipalUtil.getAuthenticatedUser();
		String newCode = sequenceService.getNewKey("");
		Document document = new Document();
		document.setDocumentCode(newCode);
		Office officeSource = officeRepo.findById(request.getSourceId())
				.orElseThrow(() -> new ApiException("Source do not exist", HttpStatus.NOT_FOUND));
		document.setSource(officeSource);
		document.setStatus(DocumentStatus.PENDING);
		document.setTitle(request.getTitle());
		document.setDescription(request.getDescription());
		document.setCreatedAt(LocalDateTime.now());
		document.setCreatedBy(principal);

		if (principal.getOffice().getDepartment().equals(officeSource.getDepartment())) {
			document.setSourceType(SourceType.INTERNAL);
		} else {
			document.setSourceType(SourceType.EXTERNAL);
		}

		documentRepo.save(document);

		if ((request.getAttachment() == null || request.getAttachment().isEmpty())
				&& (request.getAttachments() == null || request.getAttachments().isEmpty())) {
			throw new FieldValidationException("Attachment cannot be empty", "attachments");
		}

		List<UploadedFile> uploadedFiles = new ArrayList<>();
		if (request.getAttachment() != null && !request.getAttachment().isEmpty()) {
			uploadedFiles.add(fileStorageService.uploadFile(request.getAttachment(), attachmentSubFolder));
		}

		if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
			uploadedFiles.addAll(fileStorageService.uploadFiles(request.getAttachments(), attachmentSubFolder));
		}

		List<DocumentAttachment> docAttachments = new ArrayList<>();
		for (UploadedFile uploadedFile : uploadedFiles) {
			DocumentAttachment docAttachment = new DocumentAttachment();
			docAttachment.setDocument(document);
			docAttachment.setExtension(uploadedFile.getExtension());
			docAttachment.setOriginalName(uploadedFile.getOriginalName());
			docAttachment.setStoredName(uploadedFile.getStoredName());
			docAttachment.setContentType(uploadedFile.getContentType());
			docAttachment.setFilePath(uploadedFile.getFilePath());

			docAttachments.add(docAttachment);
		}
		docAttachmentRepo.saveAll(docAttachments);

		// Document action
		DocumentLog docAction = new DocumentLog();
		docAction.setAction(ActionTypes.ENCODED);
		docAction.setDocument(document);
		docAction.setSourceType(EntityType.OFFICE);
		docAction.setFromId(officeSource.getId());
		docAction.setToId(principal.getOffice().getId());
		docAction.setTargetType(EntityType.OFFICE);
		docAction.setCreatedBy(principal);
		docActionRepo.save(docAction);

		document.setActiveLog(docAction);

		documentRepo.save(document);
	}

	@PreAuthorize("hasRole('ENCODER')")
	public List<DocumentDto> fetchAll() {
		List<Document> documents = documentRepo.findAll();
		return docMapper.toDtoList(documents);
	}

	@PreAuthorize("hasRole('ENCODER')")
	public List<DocumentDto> fetchByFilteredStatus(DocumentStatus status) {
		List<Document> documents = documentRepo.findByStatus(status);
		return docMapper.toDtoList(documents);
	}

	public Document fetchByIdOrFail(Long documentId) {
		return documentRepo.findById(documentId).orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));
	}

	public List<DocumentDto> fetchCreatedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findByCreatedById(user.getId());
		return docMapper.toDtoList(documents);
	}

	public DocumentDto fetchDocumentDetails(Long documentId) {
		Document document = documentRepo.findById(documentId)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));
		return docMapper.toDto(document);
	}

	@Transactional
	public DocumentFullDto fetchDocumentFullDetails(Long id) {
		Document doc = documentRepo.findById(id)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));
		DocumentFullDto docDto = docMapper.toFullDto(doc);

		// Document attachments
		List<DocumentAttachment> docAttachments = docAttachmentRepo.findByDocumentId(id);
		docDto.setAttachments(docAttachMapper.toDtoList(docAttachments));
		return docDto;
	}

	public List<DocumentDto> fetchEncodedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findEncodedDocuments(user.getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchForwardedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findForwardedDocuments(user.getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchOfficeForwardedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findOfficeForwardedDocuments(user.getOffice().getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchOfficePendingDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findOfficePendingDocuments(user.getOffice().getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchOfficeReceivedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findOfficeReceivedDocuments(user.getOffice().getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchOfficeRevertedDocuments(){
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findOfficeRevertedDocuments(user.getOffice().getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchUserForwardedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findUserForwardedDocuments(user.getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchUserPendingDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findUserPendingDocuments(user.getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchUserReceivedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findUserReceivedDocuments(user.getId());
		return docMapper.toDtoList(documents);
	}

	public List<DocumentDto> fetchUserRevertedDocuments() {
		User user = PrincipalUtil.getAuthenticatedUser();
		List<Document> documents = documentRepo.findUserRevertedDocuments(user.getId());
		return docMapper.toDtoList(documents);
	}


}
