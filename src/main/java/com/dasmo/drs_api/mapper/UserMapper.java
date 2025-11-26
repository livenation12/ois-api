package com.dasmo.drs_api.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.dasmo.drs_api.dto.UserDto;
import com.dasmo.drs_api.dto.UserMinDto;
import com.dasmo.drs_api.model.Role;
import com.dasmo.drs_api.model.User;

@Mapper(componentModel = "spring", uses = { OfficeMapper.class })
public interface UserMapper extends BaseMapper<User, UserDto> {
	//UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserMinDto toMinDto(User user);

	List<UserMinDto> toMinDtoList(List<User> user);

	default Set<String> toStringSet(Set<Role> roles) {
		return roles.stream()
				.map(Role::getName)
				.collect(Collectors.toSet());
	}
}
