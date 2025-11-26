package com.dasmo.drs_api.exception;

public class FieldValidationException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String field;

	public FieldValidationException(String message, String field) {
		super(message);
		this.field = field;
	}

	public String getField() {
		return field;
	}
}
