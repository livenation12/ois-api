package com.dasmo.drs_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dasmo.drs_api.dto.RoutingSlipDto;
import com.dasmo.drs_api.model.RoutingSlip;

@Mapper(componentModel = "spring")
public interface RoutingSlipMapper extends BaseMapper<RoutingSlip, RoutingSlipDto> {

	RoutingSlipMapper INSTANCE = Mappers.getMapper(RoutingSlipMapper.class);
}
