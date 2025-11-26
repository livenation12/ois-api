package com.dasmo.drs_api.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.dasmo.drs_api.constants.RoutingAction;

@Component
public class RoutingActionConverter implements Converter<String, RoutingAction> {

	@Override
	public RoutingAction convert(String source) {
		return RoutingAction.valueOf(source.trim().toUpperCase());
	}
}
