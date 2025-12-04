package com.dasmo.drs_api.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.dasmo.drs_api.constants.NotificationType;
import com.dasmo.drs_api.constants.EntityType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class NotificationDto {
	private Long id;

	private String title;

	private UserMinDto sender;

	private Long targetId;

	private String targetName;

	private NotificationType type;

	private String message;

	private EntityType targetType;

	private Map<String, Object> additionalData;

	@JsonFormat(pattern = "MMM dd", timezone = "Asia/Manila")
	private LocalDateTime createdAt;

	private Boolean isUnread;

}
