package com.pi.alerteservice.repository;

import com.pi.alerteservice.model.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRuleRepository
        extends JpaRepository<AlertRule, Long> {

    List<AlertRule> findByZoneIdOrderByIdAsc(String zoneId);

    List<AlertRule> findByEnabledTrue();

    List<AlertRule> findByZoneIdAndEnabledTrue(String zoneId);
}
