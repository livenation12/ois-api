package com.dasmo.drs_api.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dasmo.drs_api.dto.query.NotificationQueryDto;
import com.dasmo.drs_api.model.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

	@Query("""
			    SELECT COUNT(n)
			    FROM Notification n
			    LEFT JOIN n.recipients r
			    WHERE (n.targetType = 'USER' AND n.targetId = :userId)
			    	OR
			        (n.targetType = 'OFFICE' AND n.targetId = :officeId)
			        AND
			        (r.recipientId IS NULL OR r.recipientId != :userId)
			""")
	Integer countUnread(@Param("userId") Long userId, @Param("officeId") Long officeId);

	@Query("""
			    SELECT
			    	n AS notification,
				    CASE
				    	WHEN r.id IS NULL THEN 1 ELSE 0 END
				    AS isUnread
			    FROM Notification n
			    LEFT JOIN n.recipients r
			    WHERE
			        (n.targetType = 'OFFICE' AND n.targetId = :officeId)
			        OR
			        (n.targetType = 'USER' AND n.targetId = :userId)
			    ORDER BY n.createdAt DESC
			""")
	List<NotificationQueryDto> findRecent(@Param("userId") Long userId, @Param("officeId") Long officeId,
			Pageable pageable);

	@Query("""
			SELECT n FROM Notification n
			LEFT JOIN n.recipients r
				WHERE
			    (n.targetType = 'USER' AND n.targetId = :userId)
			        OR
			    (n.targetType = 'OFFICE' AND n.targetId = :officeId)
			       AND
			       (r.recipientId IS NULL OR r.recipientId != :userId)
			""")
	List<Notification> findUnreadByUserAndOffice(@Param("userId") Long userId, @Param("officeId") Long officeId);
}
