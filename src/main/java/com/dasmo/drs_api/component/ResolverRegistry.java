package com.dasmo.drs_api.component;

import org.springframework.stereotype.Component;

import com.dasmo.drs_api.repo.OfficeRepo;
import com.dasmo.drs_api.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResolverRegistry {
	public final UserRepo userRepo;
	public final OfficeRepo officeRepo;
}
