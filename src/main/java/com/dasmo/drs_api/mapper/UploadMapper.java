package com.dasmo.drs_api.mapper;

import org.mapstruct.Mapper;

import com.dasmo.drs_api.dto.UploadedFile;
import com.dasmo.drs_api.model.RoutingSlipAttachment;

@Mapper(componentModel = "spring")
public interface UploadMapper {

	//UploadMapper INSTANCE = Mappers.getMapper(UploadMapper.class);

	RoutingSlipAttachment toModel(UploadedFile uploadedFile);
}
