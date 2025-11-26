package com.dasmo.drs_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dasmo.drs_api.model.NotificationRecipient;

@Repository
public interface NotificationRecipientRepo extends JpaRepository<NotificationRecipient, Long> {

}
