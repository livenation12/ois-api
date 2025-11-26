package com.dasmo.drs_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dasmo.drs_api.dto.OfficeDto;
import com.dasmo.drs_api.dto.request.OfficeRequest;
import com.dasmo.drs_api.mapper.OfficeMapper;
import com.dasmo.drs_api.model.Office;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.repo.OfficeRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfficeService {

	private final OfficeRepo officeRepo;
	private final OfficeMapper officeMapper;

	public void create(OfficeRequest request) {
		Office office = new Office();
		office.setCode(request.getCode());
		office.setDepartment(request.getDepartment());
		office.setName(request.getName());
		officeRepo.save(office);
	}

	public List<OfficeDto> fetchAllExceptPrincipalOffice(){
		User user = PrincipalUtil.getAuthenticatedUser();
		if(user.getOffice() == null) {
			return listAll();
		}
		List<Office> offices = officeRepo.findAllExceptOfficeIds(List.of(user.getOffice().getId()));
		return officeMapper.toDtoList(offices);
	}

	public List<OfficeDto> listAll(){
		List<Office> offices = officeRepo.findAll();
		return officeMapper.toDtoList(offices);
	}
}
