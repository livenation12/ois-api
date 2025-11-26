package com.dasmo.drs_api.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SequenceId implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String prefix;
	private int year;
	private int month;

	public SequenceId(String prefix, int year, int month) {
		this.prefix = prefix;
		this.year = year;
		this.month = month;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SequenceId)) {
			return false;
		}
		SequenceId that = (SequenceId) o;
		return year == that.year && month == that.month && Objects.equals(prefix, that.prefix);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prefix, year, month);
	}

}