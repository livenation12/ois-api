
package com.dasmo.drs_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.dasmo.drs_api.component.TargetResolver;
import com.dasmo.drs_api.dto.DocumentLogDto;
import com.dasmo.drs_api.dto.DocumentLogMinDto;
import com.dasmo.drs_api.model.DocumentLog;

@Mapper(componentModel = "spring", uses = { UserMapper.class, OfficeMapper.class, RoutingSlipMapper.class })
public abstract class DocumentLogMapper implements BaseMapper<DocumentLog, DocumentLogDto> {

	@Autowired
	protected TargetResolver targetResolver;

	@Override
	@Mapping(source = "document.id", target = "documentId")
	public abstract DocumentLogDto toDto(DocumentLog logs);

	@Mapping(target = "from", expression = "java(targetResolver.resolveFrom(log))")
	public abstract DocumentLogMinDto toMinDto(DocumentLog log);
}
