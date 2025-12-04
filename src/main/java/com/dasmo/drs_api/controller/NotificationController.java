package com.dasmo.drs_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dasmo.drs_api.dto.NotificationDto;
import com.dasmo.drs_api.payload.ApiResponse;
import com.dasmo.drs_api.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notifService;

	@PostMapping("/mark-all-as-read")
	public ResponseEntity<ApiResponse<Object>> doMarkAllAsRead(){
		notifService.doMarkAllAsRead();
		return ApiResponse.success("All unread notifications marked as read successfully");
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Object>> getNotifications()
	{
		List<NotificationDto> notifs = notifService.fetchNotifications();
		return ApiResponse.success(notifs);
	}

	@GetMapping("/unread-count")
	public ResponseEntity<ApiResponse<Object>> getUnreadCount(){
		Integer unreadCount = notifService.fetchUnreadCount();
		return ApiResponse.success(unreadCount);
	}

	@GetMapping("/user-office/unread-count")
	public ResponseEntity<ApiResponse<Object>> getUserOfficeUnreadCount(){
		Integer unreadCount = notifService.fetchUserOfficeUnreadCount();
		return ApiResponse.success(unreadCount);
	}

	@GetMapping("/user/unread-count")
	public ResponseEntity<ApiResponse<Object>> getUserUnreadCount(){
		Integer unreadCount = notifService.fetchUserUnreadCount();
		return ApiResponse.success(unreadCount);
	}
}
