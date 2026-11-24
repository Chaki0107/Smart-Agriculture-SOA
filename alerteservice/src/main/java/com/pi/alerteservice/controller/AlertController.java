package com.pi.alerteservice.controller;

import com.pi.alerteservice.dto.EvaluateAlertRequest;
import com.pi.alerteservice.dto.AgriculturalPredictionRequest;
import com.pi.alerteservice.model.Alert;
import com.pi.alerteservice.service.AlertManagementService;
import com.pi.alerteservice.service.AlertNotFoundException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertManagementService service;

    public AlertController(AlertManagementService service) {
        this.service = service;
    }

    @PostMapping("/evaluate")
    public List<Alert> evaluate(
            @Valid @RequestBody EvaluateAlertRequest request) {

        return service.evaluate(request);
    }

    @GetMapping
    public List<Alert> getAlerts(
            @RequestParam(required = false) String zoneId) {

        return service.getAlerts(zoneId);
    }

    @GetMapping("/{id}")
    public Alert getAlert(@PathVariable Long id) {

        try {
            return service.getAlertById(id);
        } catch (AlertNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @PatchMapping("/{id}/acknowledge")
    public Alert acknowledge(@PathVariable Long id) {

        try {
            return service.acknowledge(id);
        } catch (AlertNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }

    @PostMapping("/evaluate-ml")
    public List<Alert> evaluateWithML(
            @Valid @RequestBody AgriculturalPredictionRequest request) {

        return service.evaluateDiseaseWithML(request);
    }
}