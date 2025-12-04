package com.dasmo.drs_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dasmo.drs_api.model.DocumentLog;

@Repository
public interface DocumentLogRepo extends JpaRepository<DocumentLog, Long>{

	@Query("""
			SELECT dl FROM Document d
			JOIN d.activeLog dl
			WHERE d.id = :docId
			""")
	DocumentLog findByDocumentActiveLogId(@Param("docId") Long docId);


	List<DocumentLog> findByDocumentIdOrderByCreatedAtDesc(Long documentId);

}
