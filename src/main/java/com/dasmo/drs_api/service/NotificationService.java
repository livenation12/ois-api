package com.dasmo.drs_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.dto.NotificationDto;
import com.dasmo.drs_api.dto.query.NotificationQueryDto;
import com.dasmo.drs_api.mapper.NotificationMapper;
import com.dasmo.drs_api.model.Notification;
import com.dasmo.drs_api.model.NotificationRecipient;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.repo.NotificationRecipientRepo;
import com.dasmo.drs_api.repo.NotificationRepo;
import com.dasmo.drs_api.repo.OfficeRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final SimpMessagingTemplate messagingTemplate;
	private final NotificationRepo notifRepo;
	private final NotificationRecipientRepo recipientRepo;
	private final UserService userService;
	private final OfficeRepo officeRepo;
	private final NotificationMapper notifMapper;

	public void broadcastNotif(Notification notif) {
		messagingTemplate.convertAndSend("/topic/notification", notif);
	}

	@Transactional
	public void doMarkAllAsRead() {
		User user = userService.fetchUserPrincipal();
		List<Notification> unreadNotifs = notifRepo.findUnreadByUserAndOffice(user.getId(), user.getOffice().getId());

		List<NotificationRecipient> recipients = new ArrayList<>();

		// for every unread notif add as recipient
		for(Notification unreadNotif : unreadNotifs) {
			NotificationRecipient notifRecipient = new NotificationRecipient();
			notifRecipient.setNotification(unreadNotif);
			notifRecipient.setRecipient(user);

			recipients.add(notifRecipient);
		}

		recipientRepo.saveAll(recipients);

	}
	public List<NotificationDto> fetchNotifications() {
		User user = PrincipalUtil.getAuthenticatedUser();
		Pageable pageRequest = PageRequest.of(0, 10);

		List<NotificationQueryDto> queryResults = notifRepo.findRecent(user.getId(), user.getOffice().getId(), pageRequest);

		return queryResults.stream()
				.map(result -> {
					NotificationDto dto = notifMapper.toDto(result.getNotification());
					dto.setIsUnread(result.getIsUnread() == 1);
					return dto;
				})
				.toList();
	}

	public Integer fetchUnreadCount() {
		User user = PrincipalUtil.getAuthenticatedUser();
		Integer unreadCount = notifRepo.countUnread(user.getId(), user.getOffice().getId());
		return unreadCount;
	}

	public Integer fetchUserOfficeUnreadCount() {
		User user = PrincipalUtil.getAuthenticatedUser();
		Integer unreadCount = notifRepo.countUserOfficeUnread(user.getId(), user.getOffice().getId());
		return unreadCount;
	}

	public Integer fetchUserUnreadCount() {
		User user = PrincipalUtil.getAuthenticatedUser();
		Integer unreadCount = notifRepo.countUserUnread(user.getId());
		return unreadCount;
	}

	public StringBuilder messageBuilder(Notification notif) {
		StringBuilder sb = new StringBuilder();
		sb.append(notif.getSender().getFullName())
		.append(" of ")
		.append(notif.getSender().getOffice().getName())
		.append(" ");
		return sb;
	}



}
