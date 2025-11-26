package com.dasmo.drs_api.dto.query;

import com.dasmo.drs_api.model.Notification;

public interface NotificationQueryDto {

	Integer getIsUnread();
	Notification getNotification();

}
