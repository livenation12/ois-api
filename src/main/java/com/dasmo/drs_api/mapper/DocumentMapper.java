package com.dasmo.drs_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dasmo.drs_api.dto.DocumentDto;
import com.dasmo.drs_api.dto.DocumentFullDto;
import com.dasmo.drs_api.model.Document;

@Mapper(componentModel = "spring", uses = { DocumentAttachmentMapper.class
		, RoutingSlipMapper.class
		, OfficeMapper.class
		, UserMapper.class
		, DocumentLogMapper.class })
public interface DocumentMapper extends BaseMapper<Document, DocumentDto> {
	//DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

	// default Set<String> mapRoles(Set<Role> roles) {
	// if (roles == null) {
	// return null;
	// }
	// return roles.stream()
	// .map(Role::getName) // or whatever field you want to map
	// .collect(Collectors.toSet());
	// }
	//
	@Override
	@Mapping(source = "source.name", target = "sourceName")
	DocumentDto toDto(Document document);

	@Override
	List<DocumentDto> toDtoList(List<Document> documents);

	DocumentFullDto toFullDto(Document document);
}
