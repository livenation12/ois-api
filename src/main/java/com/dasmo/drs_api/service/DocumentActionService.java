package com.dasmo.drs_api.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.constants.NotificationType;
import com.dasmo.drs_api.constants.RoutingAction;
import com.dasmo.drs_api.constants.SubjectType;
import com.dasmo.drs_api.constants.TargetType;
import com.dasmo.drs_api.dto.DocumentLogDto;
import com.dasmo.drs_api.dto.request.DocumentActionRequest;
import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.mapper.DocumentLogMapper;
import com.dasmo.drs_api.model.Document;
import com.dasmo.drs_api.model.DocumentLog;
import com.dasmo.drs_api.model.Notification;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.model.UserPrincipal;
import com.dasmo.drs_api.repo.DocumentActionRepo;
import com.dasmo.drs_api.repo.DocumentRepo;
import com.dasmo.drs_api.repo.NotificationRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentActionService {

	private final DocumentRepo docRepo;
	private final DocumentActionRepo docActionRepo;
	private final NotificationRepo notifRepo;
	private final UserService userService;
	private final NotificationService notifService;
	private final DocumentLogMapper docLogMapper;

	@Transactional
	public void doForwardDocument(Long documentId, DocumentActionRequest request) {
		User user = PrincipalUtil.getAuthenticatedUser();
		DocumentLog log = new DocumentLog();
		Document doc = docRepo.findById(documentId)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));
		log.setAction(RoutingAction.FORWARDED);
		log.setRemarks(request.getRemarks());
		log.setAdditionalRemarks(request.getAdditionalRemarks());
		log.setTargetType(request.getTargetType());
		log.setToId(request.getToId());
		log.setFromId(user.getId());
		log.setDocument(doc);
		log.setCreatedBy(userService.getUserPrincipal());
		docActionRepo.save(log);

		// set the forward log as document.active_log and save
		doc.setActiveLog(log);
		docRepo.save(doc);

		// notification for user/office where the document was forwarded
		Notification notif = new Notification();
		notif.setSender(userService.getUserPrincipal());
		notif.setType(NotificationType.INFO);
		notif.setTitle("Document forward");
		notif.setTargetType(request.getTargetType());
		notif.setTargetId(request.getToId());
		notif.setCreatedBy(userService.getUserPrincipal());
		String message = notifService.messageBuilder(notif).append("forwarded a document with code #")
				.append(doc.getDocumentCode()).toString();
		notif.setMessage(message);

		// additional data for notif
		Map<String, Object> additionalData = Map.of("subject", SubjectType.DOCUMENT, "subjectId", doc.getId());
		notif.setAdditionalData(additionalData);
		notifRepo.save(notif);

		notifService.broadcastNotif(notif);
	}

	@Transactional
	public void doReceiveDocument(Long documentId) {
		UserPrincipal principal = PrincipalUtil.getCurrentUserPrincipal();
		Document doc = docRepo.findById(documentId)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));

		// get sender by the current action of the document
		DocumentLog activeLog = docActionRepo.findByDocumentActiveLogId(documentId);

		DocumentLog receiveLog = new DocumentLog();
		receiveLog.setDocument(doc);
		receiveLog.setAction(RoutingAction.RECEIVED);
		receiveLog.setFromId(activeLog.getFromId());
		receiveLog.setToId(principal.getUser().getOffice().getId());
		receiveLog.setTargetType(TargetType.USER);
		receiveLog.setCreatedBy(principal.getUser());

		doc.setActiveLog(receiveLog);
		docRepo.save(doc);

		docActionRepo.save(receiveLog);

		// Notification for user who forwarded the document
		Notification notif = new Notification();
		notif.setSender(userService.getUserPrincipal());
		notif.setCreatedBy(userService.getUserPrincipal());
		Map<String, Object> additionalData = Map.of("subject", SubjectType.DOCUMENT, "subjectId", doc.getId());
		notif.setAdditionalData(additionalData);
		notif.setTargetType(TargetType.OFFICE);
		notif.setTargetId(receiveLog.getFromId());
		notif.setType(NotificationType.INFO);
		notif.setTitle("Document received");
		String message = notifService.messageBuilder(notif).append("received the forwarded document with code #")
				.append(doc.getDocumentCode()).toString();
		notif.setMessage(message);
		notifRepo.save(notif);
	}

	@Transactional
	public void doReceiveOfficeDocument(Long documentId) {
		UserPrincipal principal = PrincipalUtil.getCurrentUserPrincipal();
		Document doc = docRepo.findById(documentId)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));

		// get sender by the current action of the document
		DocumentLog activeLog = docActionRepo.findByDocumentActiveLogId(documentId);

		DocumentLog receiveLog = new DocumentLog();
		receiveLog.setDocument(doc);
		receiveLog.setAction(RoutingAction.RECEIVED);
		receiveLog.setFromId(activeLog.getFromId());
		receiveLog.setToId(principal.getUser().getOffice().getId());
		receiveLog.setTargetType(TargetType.OFFICE);
		receiveLog.setCreatedBy(principal.getUser());

		doc.setActiveLog(receiveLog);
		docRepo.save(doc);

		docActionRepo.save(receiveLog);

		// Notification for user who forwarded the document
		Notification notif = new Notification();
		notif.setSender(userService.getUserPrincipal());
		notif.setCreatedBy(userService.getUserPrincipal());
		Map<String, Object> additionalData = Map.of("subject", SubjectType.DOCUMENT, "subjectId", doc.getId());
		notif.setAdditionalData(additionalData);
		notif.setTargetType(TargetType.OFFICE);
		notif.setTargetId(receiveLog.getFromId());
		notif.setType(NotificationType.INFO);
		notif.setTitle("Document received");
		String message = notifService.messageBuilder(notif).append("received the forwarded document with code #")
				.append(doc.getDocumentCode()).toString();
		notif.setMessage(message);
		notifRepo.save(notif);
	}

	public DocumentLogDto fetchById(Long id) {
		DocumentLog docAction = docActionRepo.findById(id)
				.orElseThrow(() -> new ApiException("Document action not found", HttpStatus.NOT_FOUND));
		return docLogMapper.toDto(docAction);
	}

}
