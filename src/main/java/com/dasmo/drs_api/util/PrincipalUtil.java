package com.dasmo.drs_api.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.model.UserPrincipal;

public class PrincipalUtil {

	public static User getAuthenticatedUser() {
		UserPrincipal principal = getCurrentUserPrincipal();
		return principal != null ? principal.getUser() : null;
	}

	public static String getAuthenticatedUsername() {
		UserPrincipal principal = getCurrentUserPrincipal();
		return principal.getUsername();
	}

	public static UserPrincipal getCurrentUserPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
			return userPrincipal;
		}
		throw new ApiException("Your unauthencated", HttpStatus.UNAUTHORIZED);
	}
}