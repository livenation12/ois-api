package com.dasmo.drs_api.dto;

import java.util.Map;

import com.dasmo.drs_api.constants.RoutingAction;

import lombok.Data;

@Data
public class DocumentLogMinDto {
	private Long id;
	private Long documentId;
	private RoutingAction action;
	private Map<String, Object> from;
	private Map<String, Object> to;
}
