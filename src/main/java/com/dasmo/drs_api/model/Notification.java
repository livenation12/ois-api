package com.dasmo.drs_api.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.dasmo.drs_api.constants.NotificationType;
import com.dasmo.drs_api.constants.SubjectType;
import com.dasmo.drs_api.constants.TargetType;
import com.dasmo.drs_api.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String message;

	@Enumerated(EnumType.STRING)
	private SubjectType subjectType;

	private Long subjectId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private User sender;

	private Long targetId;

	@ManyToOne(fetch = FetchType.LAZY)
	private User createdBy;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@Enumerated(EnumType.STRING)
	private TargetType targetType;

	private LocalDateTime createdAt = LocalDateTime.now();

	@OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<NotificationRecipient> recipients;

	@Lob
	@Column(name = "addtional_data")
	private String additonalData;

	@JsonIgnore
	public Map<String, Object> getAdditionalData() {
		try {
			if (additonalData == null || additonalData.isEmpty()) {
				return null;
			}
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(additonalData, Map.class);

		} catch (IOException e) {
			throw new ApiException("Failed to parse additionalData" + e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Serialize the Map to JSON and store it in the relatedDataJson field
	@JsonIgnore
	public void setAdditionalData(Map<String, Object> additonalData) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			this.additonalData = objectMapper.writeValueAsString(additonalData);

		} catch (Exception e) {
			throw new ApiException("Additional data conversion failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
