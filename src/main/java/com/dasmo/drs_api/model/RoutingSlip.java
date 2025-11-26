package com.dasmo.drs_api.model;

import java.time.LocalDateTime;
import java.util.List;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.constants.Urgency;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "routing_slips")
@Getter
@Setter
public class RoutingSlip {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "doc_tin", unique = true, nullable = false)
	private String docTin;

	private String title;
	private String subject;

	private String action;

	@Column(name = "action_requested")
	private String actionRequested;

	@Enumerated(EnumType.STRING)
	private DocumentStatus status;

	@Enumerated(EnumType.STRING)
	private Urgency urgency;

	@ManyToOne(fetch = FetchType.LAZY)
	private User fromUser;

	@ManyToOne(fetch = FetchType.LAZY)
	private Office toOffice;

	@Column(name = "is_active", columnDefinition = "TINYINT")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Document document;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private DocumentLog docAction;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoutingSlipAttachment> attachments;

}