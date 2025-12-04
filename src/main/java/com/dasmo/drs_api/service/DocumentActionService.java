package com.dasmo.drs_api.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.constants.ActionTypes;
import com.dasmo.drs_api.constants.EntityType;
import com.dasmo.drs_api.constants.NotificationType;
import com.dasmo.drs_api.constants.SubjectType;
import com.dasmo.drs_api.dto.request.DocumentActionRequest;
import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.model.Document;
import com.dasmo.drs_api.model.DocumentLog;
import com.dasmo.drs_api.model.Notification;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.model.UserPrincipal;
import com.dasmo.drs_api.repo.DocumentLogRepo;
import com.dasmo.drs_api.repo.DocumentRepo;
import com.dasmo.drs_api.repo.NotificationRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentActionService {

	private final DocumentRepo docRepo;
	private final DocumentLogRepo docLogRepo;
	private final NotificationRepo notifRepo;
	private final UserService userService;
	private final NotificationService notifService;
	private final DocumentService docService;

	@Transactional
	public void doForwardDocument(Long documentId, DocumentActionRequest request) {
		User user = PrincipalUtil.getAuthenticatedUser();
		DocumentLog log = new DocumentLog();
		Document doc = docRepo.findById(documentId)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));
		log.setAction(ActionTypes.FORWARDED);
		log.setRemarks(request.getRemarks());
		log.setAdditionalRemarks(request.getAdditionalRemarks());
		log.setTargetType(request.getTargetType());
		log.setToId(request.getToId());
		log.setFromId(user.getId());
		log.setDocument(doc);
		log.setCreatedBy(userService.fetchUserPrincipal());
		docLogRepo.save(log);

		// set the forward log as document.active_log and save
		doc.setActiveLog(log);
		docRepo.save(doc);

		// notification for user/office where the document was forwarded
		Notification notif = new Notification();
		notif.setSender(userService.fetchUserPrincipal());
		notif.setType(NotificationType.INFO);
		notif.setTitle("Document forward");
		notif.setTargetType(request.getTargetType());
		notif.setTargetId(request.getToId());
		notif.setCreatedBy(userService.fetchUserPrincipal());
		String message = notifService.messageBuilder(notif).append("forwarded a document with code #")
				.append(doc.getDocumentCode()).toString();
		notif.setMessage(message);

		notif.setSubjectId(documentId);
		notif.setSubjectType(SubjectType.DOCUMENT);
		notifRepo.save(notif);

		notifService.broadcastNotif(notif);
	}

	@Transactional
	public void doReceiveDocument(Long documentId) {
		UserPrincipal principal = PrincipalUtil.getCurrentUserPrincipal();
		Document doc = docRepo.findById(documentId)
				.orElseThrow(() -> new ApiException("Document not found", HttpStatus.NOT_FOUND));

		// get sender by the current action of the document
		DocumentLog activeLog = docLogRepo.findByDocumentActiveLogId(documentId);

		DocumentLog receiveLog = new DocumentLog();
		receiveLog.setDocument(doc);
		receiveLog.setAction(ActionTypes.RECEIVED);
		receiveLog.setFromId(activeLog.getFromId());
		receiveLog.setToId(principal.getUser().getOffice().getId());
		receiveLog.setTargetType(EntityType.USER);
		receiveLog.setCreatedBy(principal.getUser());

		doc.setActiveLog(receiveLog);
		docRepo.save(doc);

		docLogRepo.save(receiveLog);

		// Notification for user who forwarded the document
		Notification notif = new Notification();
		notif.setSender(userService.fetchUserPrincipal());
		notif.setCreatedBy(userService.fetchUserPrincipal());
		Map<String, Object> additionalData = Map.of("subject", SubjectType.DOCUMENT, "subjectId", doc.getId());
		notif.setAdditionalData(additionalData);
		notif.setTargetType(EntityType.OFFICE);
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
		DocumentLog activeLog = docLogRepo.findByDocumentActiveLogId(documentId);

		DocumentLog receiveLog = new DocumentLog();
		receiveLog.setDocument(doc);
		receiveLog.setAction(ActionTypes.RECEIVED);
		receiveLog.setFromId(activeLog.getFromId());
		receiveLog.setToId(principal.getUser().getOffice().getId());
		receiveLog.setTargetType(EntityType.OFFICE);
		receiveLog.setCreatedBy(principal.getUser());

		doc.setActiveLog(receiveLog);
		docRepo.save(doc);

		docLogRepo.save(receiveLog);

		// Notification for user who forwarded the document
		Notification notif = new Notification();
		notif.setSender(userService.fetchUserPrincipal());
		notif.setCreatedBy(userService.fetchUserPrincipal());
		notif.setSubjectId(documentId);
		notif.setSubjectType(SubjectType.DOCUMENT);
		notif.setTargetType(EntityType.OFFICE);
		notif.setTargetId(receiveLog.getFromId());
		notif.setType(NotificationType.INFO);
		notif.setTitle("Document received");
		String message = notifService.messageBuilder(notif)
				.append("received the forwarded document with code #")
				.append(doc.getDocumentCode()).toString();
		notif.setMessage(message);
		notifRepo.save(notif);
	}

	@Transactional
	public void doRevertDocument(Long documentId, DocumentActionRequest request) {
		User user = userService.fetchUserPrincipal();
		Document doc = docService.fetchByIdOrFail(documentId);
		DocumentLog activeLog = doc.getActiveLog();

		DocumentLog revertLog = new DocumentLog();
		revertLog.setAction(ActionTypes.REVERTED);
		revertLog.setRemarks(request.getRemarks());
		revertLog.setAdditionalRemarks(request.getAdditionalRemarks());
		revertLog.setCreatedBy(user);
		revertLog.setDocument(doc);

		revertLog.setSourceType(activeLog.getTargetType());
		revertLog.setTargetType(activeLog.getSourceType());

		revertLog.setFromId(activeLog.getToId());
		revertLog.setToId(activeLog.getFromId());

		docLogRepo.save(revertLog);

		// update the active log = revert log
		doc.setActiveLog(revertLog);
		docRepo.save(doc);

		Notification notif = new Notification();
		notif.setCreatedBy(user);
		notif.setSender(user);
		notif.setTitle("Document reverted");
		notif.setSubjectId(doc.getId());
		notif.setSubjectType(SubjectType.DOCUMENT);
		notif.setTargetId(revertLog.getToId());
		notif.setTargetType(revertLog.getTargetType());
		notif.setType(NotificationType.INFO);
		notif.setMessage(notifService.messageBuilder(notif).append("reverted the document with code #")
				.append(doc.getDocumentCode()).toString());
		notifRepo.save(notif);
	}
}
