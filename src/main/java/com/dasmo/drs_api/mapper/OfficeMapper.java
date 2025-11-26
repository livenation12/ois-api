package com.dasmo.drs_api.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.dasmo.drs_api.dto.OfficeDto;
import com.dasmo.drs_api.dto.OfficeMinDto;
import com.dasmo.drs_api.model.Office;

@Mapper(componentModel = "spring")
public interface OfficeMapper {
	//OfficeMapper INSTANCE = Mappers.getMapper(OfficeMapper.class);

	OfficeDto toDto(Office office);

	List<OfficeDto> toDtoList(List<Office> offices);

	OfficeMinDto toMinDto(Office office);
}
