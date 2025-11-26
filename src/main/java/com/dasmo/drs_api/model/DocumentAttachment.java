package com.dasmo.drs_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "document_attachments")
@Data
public class DocumentAttachment {

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
	@JoinColumn(name = "document_id", nullable = false,
	foreignKey = @ForeignKey(name = "fk_doc_attachment_document",
	foreignKeyDefinition = "FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE"))
	@JsonBackReference
	private Document document;
}
