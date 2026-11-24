package com.pi.alerteservice.service;

import com.pi.alerteservice.dto.AgriculturalPredictionRequest;
import com.pi.alerteservice.dto.IoTReading;
import com.pi.alerteservice.model.Alert;
import com.pi.alerteservice.model.AlertRule;
import com.pi.alerteservice.repository.AlertRepository;
import com.pi.alerteservice.repository.AlertRuleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

@Service
public class AlertMonitoringService {

    private static final Logger log =
            LoggerFactory.getLogger(AlertMonitoringService.class);

    private final AlertRepository alertRepository;
    private final AlertRuleRepository ruleRepository;
    private final AlertManagementService alertManagementService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${iot.service.url:http://localhost:8081}")
    private String iotServiceUrl;

    private Long lastProcessedId = null;

    public AlertMonitoringService(
            AlertRepository alertRepository,
            AlertRuleRepository ruleRepository,
            AlertManagementService alertManagementService) {

        this.alertRepository = alertRepository;
        this.ruleRepository = ruleRepository;
        this.alertManagementService = alertManagementService;
    }

    @Scheduled(fixedDelayString = "${alerts.monitoring.delay-ms:10000}")
    public void monitorReadings() {

        try {
            IoTReading[] response = restTemplate.getForObject(
                    iotServiceUrl + "/api/iot/readings",
                    IoTReading[].class
            );

            if (response == null || response.length == 0) {
                return;
            }

            List<IoTReading> readings = List.of(response).stream()
                    .filter(r -> r.id() != null)
                    .sorted(Comparator.comparing(IoTReading::id))
                    .toList();

            // Au premier lancement, ignorer l'historique existant.
            if (lastProcessedId == null) {
                lastProcessedId = readings.stream()
                        .map(IoTReading::id)
                        .max(Long::compareTo)
                        .orElse(0L);

                log.info(
                        "Surveillance initialisée. Dernière mesure connue : {}",
                        lastProcessedId
                );
                return;
            }

            for (IoTReading reading : readings) {

                if (reading.id() <= lastProcessedId) {
                    continue;
                }

                try {
                    evaluateReading(reading);
                } catch (Exception e) {
                    log.error(
                            "Erreur lors du traitement de la mesure {}",
                            reading.id(),
                            e
                    );

                    // Retenter cette mesure au prochain cycle.
                    break;
                }

                lastProcessedId = reading.id();
            }

        } catch (Exception e) {
            log.error(
                    "Impossible de récupérer les mesures depuis IoTWebService",
                    e
            );
        }
    }

    private void evaluateReading(IoTReading reading) {

        // 1. Vérifier les données nécessaires à la prédiction.
        if (reading.cropType() == null
                || reading.growthStage() == null
                || reading.fieldZone() == null
                || reading.airTemperatureC() == null
                || reading.relativeHumidityPct() == null
                || reading.soilTemperatureC() == null
                || reading.soilMoisturePct() == null
                || reading.soilPh() == null
                || reading.soilEcDsM() == null
                || reading.nitrogenMgKg() == null
                || reading.phosphorusMgKg() == null
                || reading.potassiumMgKg() == null
                || reading.rainfallMm() == null
                || reading.solarRadiationWM2() == null
                || reading.windSpeedMS() == null
                || reading.leafWetnessPct() == null
                || reading.evapotranspirationMm() == null) {

            throw new IllegalArgumentException(
                    "Données agricoles manquantes pour la mesure "
                            + reading.id()
            );
        }

        // 2. Construire la requête au format attendu par FastAPI.
        AgriculturalPredictionRequest predictionRequest =
                new AgriculturalPredictionRequest(
                        reading.cropType(),
                        reading.growthStage(),
                        reading.fieldZone(),
                        reading.airTemperatureC(),
                        reading.relativeHumidityPct(),
                        reading.soilTemperatureC(),
                        reading.soilMoisturePct(),
                        reading.soilPh(),
                        reading.soilEcDsM(),
                        reading.nitrogenMgKg(),
                        reading.phosphorusMgKg(),
                        reading.potassiumMgKg(),
                        reading.rainfallMm(),
                        reading.solarRadiationWM2(),
                        reading.windSpeedMS(),
                        reading.leafWetnessPct(),
                        reading.evapotranspirationMm()
                );

        // 3. Appeler le modèle ML et créer une alerte si nécessaire.
        alertManagementService.evaluateDiseaseWithML(predictionRequest);

        log.info(
                "Prédiction ML exécutée pour la mesure {} de la zone {}",
                reading.id(),
                reading.fieldZone()
        );

        // 4. Conserver l'évaluation des règles personnalisées.
        List<AlertRule> rules =
                ruleRepository.findByZoneIdAndEnabledTrue(
                        reading.fieldZone()
                );

        for (AlertRule rule : rules) {

            Double value = getMetricValue(reading, rule.getMetric());

            if (value == null) {
                continue;
            }

            boolean triggered = switch (rule.getOperator()) {
                case "BELOW" -> value < rule.getThreshold();
                case "ABOVE" -> value > rule.getThreshold();
                default -> false;
            };

            if (!triggered) {
                continue;
            }

            String alertType = "RULE_" + rule.getId();

            boolean alreadyOpen =
                    alertRepository.existsByZoneIdAndTypeAndStatus(
                            reading.fieldZone(),
                            alertType,
                            "OPEN"
                    );

            if (alreadyOpen) {
                log.debug(
                        "Alerte déjà ouverte pour la règle {}",
                        rule.getId()
                );
                continue;
            }

            String message = String.format(
                    "%s : valeur %.2f, condition %s %.2f (zone %s)",
                    rule.getMetric(),
                    value,
                    rule.getOperator(),
                    rule.getThreshold(),
                    reading.fieldZone()
            );

            Alert alert = new Alert(
                    reading.fieldZone(),
                    alertType,
                    message,
                    rule.getSeverity()
            );

            alertRepository.save(alert);

            log.info(
                    "Alerte créée : zone={}, règle={}, valeur={}",
                    reading.fieldZone(),
                    rule.getId(),
                    value
            );
        }
    }

    private Double getMetricValue(IoTReading reading, String metric) {

        return switch (metric) {
            case "SOIL_MOISTURE" -> reading.soilMoisturePct();
            case "DISEASE_RISK" -> reading.diseaseRisk();
            case "AIR_TEMPERATURE" -> reading.airTemperatureC();
            default -> null;
        };
    }
}
