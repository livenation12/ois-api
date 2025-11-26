package com.dasmo.drs_api.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dasmo.drs_api.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Boolean existsByUsername(String username);

	@Query("""
			SELECT u FROM User u
			WHERE u.id != :userId
			""")
	List<User> findAllExcept(@Param("userId") Long id);

	Optional<User> findByUsername(String username);
}
