package com.dasmo.drs_api.model;

import java.time.LocalDateTime;

import com.dasmo.drs_api.constants.EntityType;
import com.dasmo.drs_api.constants.ActionTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "document_logs")
@Getter
@Setter
public class DocumentLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long fromId;

	private Long toId;

	@Enumerated(EnumType.STRING)
	private EntityType sourceType;

	@Enumerated(EnumType.STRING)
	private EntityType targetType;

	@Enumerated(EnumType.STRING)
	private ActionTypes action;

	@Column(name="remarks", columnDefinition = "TEXT")
	private String remarks;

	@Column(name="additional_remarks", columnDefinition = "TEXT")
	private String additionalRemarks;


	private LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "document_id")
	private Document document;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id")
	private User createdBy;

	@OneToOne(fetch = FetchType.LAZY)
	private RoutingSlip routingSlip;
}