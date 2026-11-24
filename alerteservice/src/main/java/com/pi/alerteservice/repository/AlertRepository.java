package com.pi.alerteservice.repository;

import com.pi.alerteservice.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository
        extends JpaRepository<Alert, Long> {

    List<Alert> findByZoneIdOrderByCreatedAtDesc(String zoneId);

    List<Alert> findAllByOrderByCreatedAtDesc();

    boolean existsByZoneIdAndTypeAndStatus(
            String zoneId,
            String type,
            String status
    );
}
