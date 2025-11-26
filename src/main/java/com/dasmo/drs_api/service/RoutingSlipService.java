package com.dasmo.drs_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.dto.RoutingSlipDto;
import com.dasmo.drs_api.dto.UploadedFile;
import com.dasmo.drs_api.dto.request.RoutingSlipRequest;
import com.dasmo.drs_api.exception.ApiException;
import com.dasmo.drs_api.mapper.RoutingSlipMapper;
import com.dasmo.drs_api.mapper.UploadMapper;
import com.dasmo.drs_api.model.Document;
import com.dasmo.drs_api.model.RoutingSlip;
import com.dasmo.drs_api.model.RoutingSlipAttachment;
import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.repo.DocumentRepo;
import com.dasmo.drs_api.repo.RoutingSlipRepo;
import com.dasmo.drs_api.util.PrincipalUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoutingSlipService {

	private final RoutingSlipRepo routingRepo;
	private final DocumentRepo docRepo;
	private final SequenceService seqService;
	private final FileStorageService fsService;
	private final UploadMapper uploadMapper;

	@Transactional
	public void create(RoutingSlipRequest request) {

		User user = PrincipalUtil.getAuthenticatedUser();
		RoutingSlip routing = new RoutingSlip();
		routing.setAction(request.getAction());
		routing.setActionRequested(request.getActionRequested());
		routing.setUrgency(request.getUrgency());
		routing.setSubject(request.getSubject());

		String seq = seqService.getNewKey(user.getOffice().getCode());
		routing.setDocTin(seq);

		Document doc = docRepo.findById(request.getDocumentId()).orElseThrow(() -> new ApiException("Invalid document", HttpStatus.BAD_REQUEST));
		routing.setDocument(doc);

		routingRepo.save(routing);

		UploadedFile file = fsService.uploadFile(request.getAttachment(), "/routing-slips");
		RoutingSlipAttachment routingAttachment = uploadMapper.toModel(file);
		routingAttachment.setRoutingSlip(routing);
	}

	public List<RoutingSlipDto> fetchByDocumentId(Long docId){
		List<RoutingSlip> routings = routingRepo.findByDocumentId(docId);
		return RoutingSlipMapper.INSTANCE.toDtoList(routings);
	}
}
