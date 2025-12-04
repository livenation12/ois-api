package com.dasmo.drs_api.component;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.EntityType;
import com.dasmo.drs_api.mapper.OfficeMapper;
import com.dasmo.drs_api.mapper.UserMapper;
import com.dasmo.drs_api.model.DocumentLog;
import com.dasmo.drs_api.model.Notification;
import com.dasmo.drs_api.repo.OfficeRepo;
import com.dasmo.drs_api.repo.UserRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TargetResolver {
	private final OfficeRepo officeRepo;
	private final UserRepo userRepo;
	private final UserMapper userMapper;
	private final OfficeMapper officeMapper;

	public Map<String, Object> resolveFrom(DocumentLog log) {
		if (log == null || log.getFromId() == null || log.getTargetType() == null) {
			return null;
		}

		Long fromId = log.getFromId();
		EntityType type = log.getTargetType();

		Object entity;

		switch (type) {
		case OFFICE:
			entity = officeMapper.toMinDto(officeRepo.findById(fromId).orElse(null));
			break;
		case USER:
			entity = userMapper.toMinDto(userRepo.findById(fromId).orElse(null));
			break;
		default:
			return null;
		}

		if (entity == null) {
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(entity, new TypeReference<Map<String, Object>>() {
		});
	}

	public String resolveTargetName(Notification notification) {
		if (notification == null) {
			return null;
		}

		Long id = notification.getTargetId();
		EntityType type = notification.getTargetType();
		if (id == null || type == null) {
			return null;
		}

		switch (type) {
		case USER:
			return userRepo.findById(id).map(u -> u.getFullName()).orElse(null);
		case OFFICE:
			return officeRepo.findById(id).map(o -> o.getName()).orElse(null);
		default:
			return null;
		}
	}

}
