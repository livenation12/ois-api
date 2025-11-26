package com.dasmo.drs_api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.dto.UserDto;
import com.dasmo.drs_api.dto.request.LoginRequest;
import com.dasmo.drs_api.dto.request.RegistrationRequest;
import com.dasmo.drs_api.mapper.OfficeMapper;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.model.UserPrincipal;
import com.dasmo.drs_api.repo.UserRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authManager;
	private final UserRepo userRepo;
	private final BCryptPasswordEncoder pwEncoder;
	private final OfficeMapper officeMapper;

	public UserPrincipal authenticate(LoginRequest request) {
		String username = request.getUsername();
		String password = request.getPassword();
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		System.out.println("PRINCIPAL");
		System.out.println(principal.getUsername());
		return principal;
	}

	@Transactional
	public UserDto fetchCurrentUser() {
		UserPrincipal userPrincipal = PrincipalUtil.getCurrentUserPrincipal();
		UserDto user = new UserDto();
		user.setUsername(userPrincipal.getUsername());
		user.setOffice(officeMapper.toDto(userPrincipal.getUser().getOffice()));
		user.setRoles(userPrincipal.getRoles());
		return user;
	}


	public void register(RegistrationRequest request) {
		String username = request.getUsername();
		String password = request.getPassword();
		String confirmPassword = request.getConfirmPassword();

		if(!password.equals(confirmPassword)) {
			throw new ValidationException("Passwords do not match");
		}

		Boolean isUsernameExists = userRepo.existsByUsername(username);
		if(isUsernameExists) {
			throw new ValidationException("Username already exists");
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(pwEncoder.encode(password));
		userRepo.save(user);
	}
}
