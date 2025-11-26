package com.dasmo.drs_api.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.Urgency;

@Component
public class UrgencyConverter implements Converter<String, Urgency> {

	@Override
	public Urgency convert(String source) {
		return Urgency.valueOf(source.trim().toUpperCase());
	}

}
