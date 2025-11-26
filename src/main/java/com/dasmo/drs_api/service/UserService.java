package com.dasmo.drs_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.dto.UserMinDto;
import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.mapper.UserMapper;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.repo.UserRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepo userRepo;
	private final UserMapper userMapper;

	public List<UserMinDto> fetchAllExceptPrincipal(){
		User user = PrincipalUtil.getAuthenticatedUser();
		List<User> users = userRepo.findAllExcept(user.getId());
		return userMapper.toMinDtoList(users);
	}

	//temporary
	public List<UserMinDto> fetchAllUsers(){
		List<User> users = userRepo.findAll();
		return userMapper.toMinDtoList(users);
	}

	public User getUserPrincipal() {
		User user = PrincipalUtil.getAuthenticatedUser();
		return userRepo.findById(user.getId()).orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
	}
}
