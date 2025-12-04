package com.dasmo.drs_api.constants;

import com.dasmo.drs_api.component.ResolverRegistry;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EntityType {
	OFFICE("Office", (id, r) -> r.officeRepo.findById(id).orElse(null)),
	USER("User", (id, r) -> r.userRepo.findById(id).orElse(null));

	@FunctionalInterface
	interface Resolver {
		Object resolve(Long id, ResolverRegistry registry);
	}

	private final String displayName;


	private final Resolver resolver;

	EntityType(String displayName, Resolver resolver) {
		this.displayName = displayName;
		this.resolver = resolver;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}

	public Object resolve(Long id, ResolverRegistry registry) {
		return resolver.resolve(id, registry);
	}
}
