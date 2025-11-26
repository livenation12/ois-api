package com.dasmo.drs_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.dasmo.drs_api.component.TargetResolver;
import com.dasmo.drs_api.dto.NotificationDto;
import com.dasmo.drs_api.model.Notification;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public abstract class NotificationMapper {

	@Autowired
	protected TargetResolver targetResolver;

	@Mapping(target = "targetName", expression = "java(targetResolver.resolveTargetName(notif))")
	public abstract NotificationDto toDto(Notification notif);

	public abstract List<NotificationDto> toDtoList(List<Notification> notifs);

}
