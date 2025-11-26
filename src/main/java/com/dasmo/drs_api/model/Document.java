package com.dasmo.drs_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.constants.SourceType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "document_code")
	private String documentCode;

	@Column(name = "source_type")
	@Enumerated(EnumType.STRING)
	private SourceType sourceType = SourceType.EXTERNAL;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	private Office source;

	@Enumerated(EnumType.STRING)
	private DocumentStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	private User createdBy;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "active_log_id")
	private DocumentLog activeLog;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<RoutingSlip> routings;

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<DocumentAttachment> attachments = new ArrayList<>();

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference // for bi-directional relationship
	private List<DocumentLog> logs;
}
