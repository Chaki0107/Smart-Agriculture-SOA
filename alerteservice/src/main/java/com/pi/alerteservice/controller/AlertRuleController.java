package com.pi.alerteservice.controller;

import com.pi.alerteservice.model.AlertRule;
import com.pi.alerteservice.repository.AlertRuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/api/alerts/rules")
public class AlertRuleController {

    private final AlertRuleRepository repository;

    private static final Set<String> METRICS = Set.of(
            "SOIL_MOISTURE",
            "DISEASE_RISK",
            "AIR_TEMPERATURE"
    );

    private static final Set<String> OPERATORS =
            Set.of("BELOW", "ABOVE");

    private static final Set<String> SEVERITIES =
            Set.of("INFO", "WARNING", "CRITICAL");

    public AlertRuleController(AlertRuleRepository repository) {
        this.repository = repository;
    }

    // 1. Create Alert Rule
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertRule createRule(@RequestBody AlertRule rule) {

        validateRule(rule);

        rule.setEnabled(true);

        return repository.save(rule);
    }

    // 2. Get All Alert Rules
    @GetMapping
    public List<AlertRule> getRules(
            @RequestParam(required = false) String zoneId) {

        if (zoneId == null || zoneId.isBlank()) {
            return repository.findAll();
        }

        return repository.findByZoneIdOrderByIdAsc(zoneId);
    }

    // Validation
    private void validateRule(AlertRule rule) {

        if (rule.getZoneId() == null
                || rule.getZoneId().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La zone est obligatoire");
        }

        if (rule.getMetric() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le paramètre surveillé est obligatoire");
        }

        rule.setMetric(
                rule.getMetric().toUpperCase(Locale.ROOT));

        if (!METRICS.contains(rule.getMetric())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Paramètre non pris en charge");
        }

        if (rule.getOperator() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'opérateur est obligatoire");
        }

        rule.setOperator(
                rule.getOperator().toUpperCase(Locale.ROOT));

        if (!OPERATORS.contains(rule.getOperator())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Opérateur attendu : BELOW ou ABOVE");
        }

        if (rule.getThreshold() == null
                || !Double.isFinite(rule.getThreshold())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le seuil doit être un nombre valide");
        }

        if (rule.getSeverity() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La gravité est obligatoire");
        }

        rule.setSeverity(
                rule.getSeverity().toUpperCase(Locale.ROOT));

        if (!SEVERITIES.contains(rule.getSeverity())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Gravité attendue : INFO, WARNING ou CRITICAL");
        }

        if (rule.getMetric().equals("SOIL_MOISTURE")
                && (rule.getThreshold() < 0
                || rule.getThreshold() > 100)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'humidité doit être comprise entre 0 et 100");
        }

        if (rule.getMetric().equals("DISEASE_RISK")
                && (rule.getThreshold() < 0
                || rule.getThreshold() > 1)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le risque doit être compris entre 0 et 1");
        }
    }
}