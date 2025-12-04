
package com.dasmo.drs_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.dasmo.drs_api.component.ResolverRegistry;
import com.dasmo.drs_api.constants.EntityType;
import com.dasmo.drs_api.dto.DocumentLogDto;
import com.dasmo.drs_api.dto.DocumentLogMinDto;
import com.dasmo.drs_api.model.DocumentLog;
import com.dasmo.drs_api.model.Office;
import com.dasmo.drs_api.model.User;

@Mapper(componentModel = "spring", uses = { UserMapper.class, OfficeMapper.class, RoutingSlipMapper.class })
public abstract class DocumentLogMapper implements BaseMapper<DocumentLog, DocumentLogDto> {

	@Autowired
	protected ResolverRegistry registry;
	@Autowired
	protected UserMapper userMapper;
	@Autowired
	protected OfficeMapper officeMapper;

	protected Object resolveFrom(DocumentLog log) {
		EntityType sourceType = log.getSourceType();
		Long fromId = log.getFromId();
		if(sourceType == null || fromId == null) {
			return null;
		}
		Object from = sourceType.resolve(fromId, registry);
		return toDtoSafe(from);
	}

	protected Boolean resolveIsActive(DocumentLog log) {
		return log.getId().equals(log.getDocument().getActiveLog().getId());
	}
	protected Object resolveTo(DocumentLog log) {
		EntityType sourceType = log.getSourceType();
		Long toId = log.getToId();
		if(sourceType == null || toId == null) {
			return null;
		}
		Object to = sourceType.resolve(toId, registry);
		return toDtoSafe(to);
	}

	@Override
	@Mapping(source = "document.id", target = "documentId")
	@Mapping(target = "from", expression = "java(resolveFrom(log))")
	@Mapping(target = "to", expression = "java(resolveTo(log))")
	public abstract DocumentLogDto toDto(DocumentLog log);

	protected Object toDtoSafe(Object entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof User user) {
			return userMapper.toDto(user);
		}

		if (entity instanceof Office office) {
			return officeMapper.toDto(office);
		}

		return entity;
	}

	@Mapping(source = "document.id", target = "documentId")
	@Mapping(target = "from", expression = "java(resolveFrom(log))")
	@Mapping(target = "to", expression = "java(resolveTo(log))")
	@Mapping(target = "isActive", expression = "java(resolveIsActive(log))")
	public abstract DocumentLogMinDto toMinDto(DocumentLog log);

	public abstract List<DocumentLogMinDto> toMinDtoList(List<DocumentLog> log);
}
