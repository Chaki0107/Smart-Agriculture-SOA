package com.pi.iotwebservice.repository;

import com.pi.iotwebservice.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepository
        extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findBySensorNodeId(String sensorNodeId);

    List<SensorReading> findByFieldZone(String fieldZone);

    List<SensorReading> findByCropType(String cropType);

    List<SensorReading> findBySensorStatus(String sensorStatus);
}

