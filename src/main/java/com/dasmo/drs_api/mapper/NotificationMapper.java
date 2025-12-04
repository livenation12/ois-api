package com.dasmo.drs_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.dasmo.drs_api.component.ResolverRegistry;
import com.dasmo.drs_api.constants.EntityType;
import com.dasmo.drs_api.dto.NotificationDto;
import com.dasmo.drs_api.model.Notification;
import com.dasmo.drs_api.model.Office;
import com.dasmo.drs_api.model.User;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public abstract class NotificationMapper {

	@Autowired
	private ResolverRegistry registry;

	protected String resolveTargetName(Notification notif) {
		if (notif == null) {
			return null;
		}

		Long id = notif.getTargetId();
		EntityType type = notif.getTargetType();
		if (id == null || type == null) {
			return null;
		}

		Object entity = type.resolve(id, registry);
		if (entity == null) {
			return null;
		}

		if (entity instanceof User u) {
			return u.getFullName();
		}
		if (entity instanceof Office o) {
			return o.getName();
		}

		return null;
	}


	@Mapping(target = "targetName", expression = "java(resolveTargetName(notif))")
	public abstract NotificationDto toDto(Notification notif);

	public abstract List<NotificationDto> toDtoList(List<Notification> notifs);

}
