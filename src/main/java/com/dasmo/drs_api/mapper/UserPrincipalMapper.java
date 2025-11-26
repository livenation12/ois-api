package com.dasmo.drs_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dasmo.drs_api.dto.UserDto;
import com.dasmo.drs_api.model.UserPrincipal;

@Mapper(componentModel = "spring")
public interface UserPrincipalMapper extends BaseMapper<UserPrincipal, UserDto> {
	@Override
	@Mapping(source = "user.username", target = "username")
	@Mapping(source = "user.office", target = "office")
	@Mapping(source = "user.fullName", target = "fullName")
	UserDto toDto(UserPrincipal user);
}
