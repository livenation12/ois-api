package com.dasmo.drs_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dasmo.drs_api.model.Office;

public interface OfficeRepo extends JpaRepository<Office, Long>{

	@Query("""
			    SELECT o FROM Office o
			    WHERE o.id NOT IN :ids
			""")
	List<Office> findAllExceptOfficeIds(@Param("ids") List<Long> ids);

}
