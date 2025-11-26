package com.dasmo.drs_api.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.DocumentStatus;

@Component
public class DocumentStatusConverter implements Converter<String, DocumentStatus> {

	@Override
	public DocumentStatus convert(String source) {
		return DocumentStatus.valueOf(source.trim().toUpperCase());
	}
}
