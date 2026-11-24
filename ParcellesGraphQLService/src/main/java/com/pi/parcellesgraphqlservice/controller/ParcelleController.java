
package com.pi.parcellesgraphqlservice.controller;

import com.pi.parcellesgraphqlservice.model.IrrigationRecord;
import com.pi.parcellesgraphqlservice.model.Parcelle;
import com.pi.parcellesgraphqlservice.model.SensorData;
import com.pi.parcellesgraphqlservice.repository.SensorDataRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ParcelleController {

    private final SensorDataRepository repository;

    public ParcelleController(SensorDataRepository repository) {
        this.repository = repository;
    }

    // 1. Liste des zones disponibles
    @QueryMapping
    public List<String> zones() {
        return repository.findDistinctZones();
    }

    // 2. Liste des cultures disponibles
    @QueryMapping
    public List<String> cultures() {
        return repository.findDistinctCrops();
    }

    // 3. Informations sur une zone précise
    @QueryMapping
    public Parcelle parcelle(@Argument String zoneId) {

        Optional<SensorData> latest =
                repository.findFirstByFieldZoneOrderByTimestampDescIdDesc(
                        zoneId
                );

        if (latest.isEmpty()) {
            return null;
        }

        SensorData data = latest.get();

        List<String> cultures =
                repository.findCropsByZone(zoneId);

        return new Parcelle(
                zoneId,
                cultures,
                data.getLatitude(),
                data.getLongitude(),
                data.getGrowthStage()
        );
    }

    // 4. Historique récent d'irrigation
    @QueryMapping
    public List<IrrigationRecord> historiqueIrrigation(
            @Argument String zoneId) {

        return repository
                .findByFieldZoneAndIrrigationStatusIsNotNullOrderByTimestampDescIdDesc(
                        zoneId,
                        PageRequest.of(0, 100)
                )
                .stream()
                .map(data -> new IrrigationRecord(
                        String.valueOf(data.getId()),
                        data.getFieldZone(),
                        data.getTimestamp() == null
                                ? null
                                : data.getTimestamp().toString(),
                        data.getIrrigationStatus(),
                        data.getIrrigationAmountL(),
                        data.getWaterFlowRateLMin()
                ))
                .toList();
    }
}
