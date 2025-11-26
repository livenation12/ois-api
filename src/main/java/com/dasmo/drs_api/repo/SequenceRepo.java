package com.dasmo.drs_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dasmo.drs_api.model.Sequence;
import com.dasmo.drs_api.model.SequenceId;

public interface SequenceRepo extends JpaRepository<Sequence, SequenceId> {

	@Query("""
			    SELECT COALESCE(MAX(s.lastNumber), 0)
			    FROM Sequence s
			    WHERE s.prefix = :prefix
			      AND s.year = :year
			      AND s.month = :month
			""")
	Integer findLastBySequenceId(
			@Param("prefix") String prefix,
			@Param("year") int year,
			@Param("month") int month
			);

}
