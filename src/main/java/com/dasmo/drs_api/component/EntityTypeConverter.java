package com.dasmo.drs_api.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.EntityType;

@Component
public class EntityTypeConverter implements Converter<String, EntityType> {

	@Override
	public EntityType convert(String type) {
		return EntityType.valueOf(type.trim().toUpperCase());
	}

}
