package com.dasmo.drs_api.mapper;

import org.mapstruct.Mapper;

import com.dasmo.drs_api.dto.AttachmentDto;
import com.dasmo.drs_api.model.DocumentAttachment;

@Mapper(componentModel = "spring")
public interface DocumentAttachmentMapper extends BaseMapper<DocumentAttachment, AttachmentDto> {
	//DocumentAttachmentMapper INSTANCE = Mappers.getMapper(DocumentAttachmentMapper.class);
}
