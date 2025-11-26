package com.dasmo.drs_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sequences")
@Getter
@Setter
@IdClass(SequenceId.class)
public class Sequence {
	@Id
	private String prefix;

	@Id
	private int year;

	@Id
	private int month;

	@Column(name = "last_number")
	private int lastNumber;
}