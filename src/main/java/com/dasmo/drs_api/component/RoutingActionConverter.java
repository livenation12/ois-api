package com.dasmo.drs_api.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.ActionTypes;

@Component
public class RoutingActionConverter implements Converter<String, ActionTypes> {

	@Override
	public ActionTypes convert(String source) {
		return ActionTypes.valueOf(source.trim().toUpperCase());
	}
}
