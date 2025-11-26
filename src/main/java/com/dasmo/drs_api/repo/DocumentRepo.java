package com.dasmo.drs_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dasmo.drs_api.constants.DocumentStatus;
import com.dasmo.drs_api.model.Document;

public interface DocumentRepo extends JpaRepository<Document, Long> {

	@Override
	@EntityGraph(attributePaths = "attachments")
	List<Document> findAll();

	// @Query("""
	// SELECT DISTINCT d FROM DocumentAction da
	// LEFT JOIN da.document d
	// LEFT JOIN FETCH d.attachments
	// WHERE da.action = :action
	// AND da.toOffice.id = :officeId
	// """)
	// List<Document> findByActionAndFromOfficeIdWithAttachments(@Param("action")
	// RoutingAction action,
	// @Param("officeId") Long officeId);

	/*
	 * @Query(""" SELECT DISTINCT d FROM DocumentAction da LEFT JOIN da.document d
	 * LEFT JOIN FETCH d.attachments WHERE da.action = :action AND da.toOffice.id =
	 * :officeId """) List<Document>
	 * findByActionAndToOfficeIdWithAttachments(@Param("action") RoutingAction
	 * action,
	 *
	 * @Param("officeId") Long officeId);
	 */

	List<Document> findByCreatedById(Long createdById);

	List<Document> findByStatus(DocumentStatus status);

	@Query("""
			SELECT DISTINCT d FROM Document d
				JOIN d.activeLog al
			WHERE al.action = 'ENCODED'
				AND al.createdBy.id = :userId
			ORDER BY d.createdAt DESC
			""")
	List<Document> findEncodedDocuments(@Param("userId") Long id);

	@Query("""
				SELECT DISTINCT d FROM Document d
					JOIN d.logs dl
				WHERE dl.action = 'FORWARDED'
					AND dl.createdBy.id = :userId
				ORDER BY dl.createdAt DESC
			""")
	List<Document> findForwardedDocuments(@Param("userId") Long userId);

	@Query("""
				SELECT DISTINCT d FROM DocumentLog dl
					JOIN dl.document d
				WHERE dl.action = 'FORWARDED'
					AND dl.fromId = :officeId
					AND dl.targetType = 'OFFICE'
				ORDER BY dl.createdAt DESC
			""")
	List<Document> findOfficeForwardedDocuments(@Param("officeId") Long officeId);

	@Query("""
				SELECT d FROM Document d
					JOIN d.activeLog al
				WHERE al.action = 'FORWARDED'
					AND al.targetType = 'OFFICE'
					AND al.toId = :officeId
				ORDER BY al.createdAt DESC

			""")
	List<Document> findOfficePendingDocuments(@Param("officeId") Long officeId);

	@Query("""
			SELECT DISTINCT d FROM Document d
				JOIN d.activeLog al
			WHERE al.action = 'RECEIVED'
				AND al.targetType = 'OFFICE'
				AND al.toId = :officeId
			ORDER BY al.createdAt DESC
			""")
	List<Document> findOfficeReceivedDocuments(@Param("officeId") Long officeId);

	@Query("""
			SELECT DISTINCT d FROM DocumentLog dl
				JOIN dl.document d
			WHERE dl.action = 'FORWARDED'
				AND dl.fromId = :userId
				AND dl.targetType = 'USER'
			ORDER BY dl.createdAt DESC
			""")
	List<Document> findUserForwardedDocuments(@Param("userId") Long userId);

	@Query("""
			SELECT DISTINCT d FROM Document d
				JOIN d.activeLog al
			WHERE al.action = 'FORWARDED'
				AND al.toId = :userId
				AND al.targetType = 'USER'
			ORDER BY al.createdAt DESC
			""")
	List<Document> findUserPendingDocuments(@Param("userId") Long userId);

	@Query("""
			SELECT DISTINCT d FROM Document d
				JOIN d.activeLog al
			WHERE al.action = 'RECEIVED'
				AND al.toId = :userId
				AND al.targetType = 'USER'
			ORDER BY al.createdAt DESC
			""")
	List<Document> findUserReceivedDocuments(@Param("userId") Long userId);


}
