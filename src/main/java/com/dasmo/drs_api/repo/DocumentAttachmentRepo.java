package com.dasmo.drs_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dasmo.drs_api.model.DocumentAttachment;

@Repository
public interface DocumentAttachmentRepo extends JpaRepository<DocumentAttachment, Long> {

	List<DocumentAttachment> findByDocumentId(Long docId);
}
