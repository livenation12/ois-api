package com.dasmo.drs_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="routing_attachments")
@Data
public class RoutingSlipAttachment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "stored_name")
	private String storedName;

	private String originalName;

	private String extension;

	private String contentType;

	private String filePath;

	@ManyToOne
	@JoinColumn(name = "routing_slip_id", nullable = false)
	private RoutingSlip routingSlip;
}
