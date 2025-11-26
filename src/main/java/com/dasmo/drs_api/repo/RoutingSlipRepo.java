package com.dasmo.drs_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dasmo.drs_api.model.RoutingSlip;

@Repository
public interface RoutingSlipRepo extends JpaRepository<RoutingSlip, Long> {

	List<RoutingSlip> findByDocumentId(Long id);
}
