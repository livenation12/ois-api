package com.dasmo.drs_api.dto.request;

import com.dasmo.drs_api.constants.DocumentStatus;

import lombok.Data;

@Data
public class DocumentRequestParam {

	private DocumentStatus status;
}
