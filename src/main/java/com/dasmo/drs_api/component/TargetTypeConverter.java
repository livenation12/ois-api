package com.dasmo.drs_api.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.TargetType;

@Component
public class TargetTypeConverter implements Converter<String, TargetType> {

	@Override
	public TargetType convert(String type) {
		return TargetType.valueOf(type.trim().toUpperCase());
	}

}
