package com.pi.iotwebservice.controller;

import com.pi.iotwebservice.SensorReading;
import com.pi.iotwebservice.repository.SensorReadingRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iot")
public class IoTController {

    private final SensorReadingRepository repository;

    public IoTController(SensorReadingRepository repository) {
        this.repository = repository;
    }

    // 1. Ajouter une nouvelle mesure
    @PostMapping("/readings")
    public ResponseEntity<SensorReading> createReading(
            @RequestBody SensorReading reading) {

        SensorReading savedReading = repository.save(reading);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedReading);
    }

    // 2. Récupérer toutes les mesures
    @GetMapping("/readings")
    public List<SensorReading> getAllReadings() {
        return repository.findAll();
    }

    // 3. Récupérer une mesure par son identifiant
    @GetMapping("/readings/{id}")
    public ResponseEntity<SensorReading> getReadingById(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Récupérer les mesures d'un capteur
    @GetMapping("/sensors/{sensorId}")
    public List<SensorReading> getReadingsBySensor(
            @PathVariable String sensorId) {

        return repository.findBySensorNodeId(sensorId);
    }

    // 5. Récupérer les mesures d'une zone agricole
    @GetMapping("/zones/{zoneId}")
    public List<SensorReading> getReadingsByZone(
            @PathVariable String zoneId) {

        return repository.findByFieldZone(zoneId);
    }

    // 6. Filtrer les mesures par culture
    @GetMapping("/crops/{cropType}")
    public List<SensorReading> getReadingsByCrop(
            @PathVariable String cropType) {

        return repository.findByCropType(cropType);
    }

    // 7. Filtrer les mesures par état du capteur
    @GetMapping("/sensors/status/{status}")
    public List<SensorReading> getReadingsByStatus(
            @PathVariable String status) {

        return repository.findBySensorStatus(status);
    }
}