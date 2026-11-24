
package com.pi.parcellesgraphqlservice.repository;

import com.pi.parcellesgraphqlservice.model.SensorData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SensorDataRepository
        extends JpaRepository<SensorData, Long> {

    @Query("""
        SELECT DISTINCT s.fieldZone
        FROM SensorData s
        WHERE s.fieldZone IS NOT NULL
        ORDER BY s.fieldZone
        """)
    List<String> findDistinctZones();

    @Query("""
        SELECT DISTINCT s.cropType
        FROM SensorData s
        WHERE s.cropType IS NOT NULL
        ORDER BY s.cropType
        """)
    List<String> findDistinctCrops();

    Optional<SensorData>
    findFirstByFieldZoneOrderByTimestampDescIdDesc(String fieldZone);

    @Query("""
        SELECT DISTINCT s.cropType
        FROM SensorData s
        WHERE s.fieldZone = :zoneId
          AND s.cropType IS NOT NULL
        ORDER BY s.cropType
        """)
    List<String> findCropsByZone(
            @Param("zoneId") String zoneId
    );

    List<SensorData>
    findByFieldZoneAndIrrigationStatusIsNotNullOrderByTimestampDescIdDesc(
            String fieldZone,
            Pageable pageable
    );
}
