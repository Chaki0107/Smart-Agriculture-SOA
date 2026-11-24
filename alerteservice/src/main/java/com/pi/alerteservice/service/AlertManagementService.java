
package com.pi.alerteservice.service;

import com.pi.alerteservice.dto.AgriculturalPredictionRequest;
import com.pi.alerteservice.dto.DiseasePredictionResponse;
import com.pi.alerteservice.dto.EvaluateAlertRequest;
import com.pi.alerteservice.model.Alert;
import com.pi.alerteservice.repository.AlertRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertManagementService {

    private final AlertRepository repository;
    private final RestTemplate restTemplate;

    private final String mlServiceUrl;
    private final double soilMoistureThreshold;
    private final double diseaseRiskThreshold;
    private final double mlDiseaseProbabilityThreshold;

    public AlertManagementService(
            AlertRepository repository,
            @Value("${ml.service.url:http://localhost:8000}")
            String mlServiceUrl,
            @Value("${alerts.soil-moisture-threshold:20.0}")
            double soilMoistureThreshold,
            @Value("${alerts.disease-risk-threshold:0.8}")
            double diseaseRiskThreshold,
            @Value("${alerts.ml-disease-probability-threshold:0.8}")
            double mlDiseaseProbabilityThreshold) {

        this.repository = repository;
        this.mlServiceUrl = mlServiceUrl;
        this.soilMoistureThreshold = soilMoistureThreshold;
        this.diseaseRiskThreshold = diseaseRiskThreshold;
        this.mlDiseaseProbabilityThreshold =
                mlDiseaseProbabilityThreshold;

        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public List<Alert> evaluate(EvaluateAlertRequest request) {

        List<Alert> generatedAlerts = new ArrayList<>();

        // 1. Vérifier l'humidité du sol
        if (request.soilMoisturePct() != null
                && request.soilMoisturePct()
                < soilMoistureThreshold) {

            createIfNotOpen(
                    request.zoneId(),
                    "LOW_SOIL_MOISTURE",
                    "Humidité du sol faible : "
                            + request.soilMoisturePct() + " %",
                    "WARNING",
                    generatedAlerts
            );
        }

        // 2. Vérifier le risque fourni dans la requête
        if (request.diseaseRisk() != null
                && request.diseaseRisk() >= diseaseRiskThreshold) {

            createIfNotOpen(
                    request.zoneId(),
                    "HIGH_DISEASE_RISK",
                    "Risque de maladie élevé : "
                            + request.diseaseRisk(),
                    "HIGH",
                    generatedAlerts
            );
        }

        return generatedAlerts;
    }

    /**
     * Évalue le risque de maladie avec le modèle ML.
     */
    @Transactional
    public List<Alert> evaluateDiseaseWithML(
            AgriculturalPredictionRequest data) {

        List<Alert> generatedAlerts = new ArrayList<>();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AgriculturalPredictionRequest> entity =
                    new HttpEntity<>(data, headers);

            DiseasePredictionResponse prediction =
                    restTemplate.postForObject(
                            mlServiceUrl + "/predict/disease",
                            entity,
                            DiseasePredictionResponse.class
                    );

            if (prediction == null) {
                throw new IllegalStateException(
                        "Le service ML a retourné une réponse vide."
                );
            }

            Double probability = prediction.class1Probability();

            // On exploite la probabilité si le modèle la fournit.
            if (probability != null
                    && probability >= mlDiseaseProbabilityThreshold) {

                createIfNotOpen(
                        data.fieldZone(),
                        "ML_HIGH_DISEASE_RISK",
                        "Le modèle ML estime une probabilité de classe 1 de "
                                + probability,
                        "HIGH",
                        generatedAlerts
                );
            }

        } catch (RestClientException exception) {
            throw new IllegalStateException(
                    "Impossible de contacter MLPredictionService à "
                            + mlServiceUrl,
                    exception
            );
        }

        return generatedAlerts;
    }

    private void createIfNotOpen(
            String zoneId,
            String type,
            String message,
            String severity,
            List<Alert> generatedAlerts) {

        boolean alreadyOpen =
                repository.existsByZoneIdAndTypeAndStatus(
                        zoneId, type, "OPEN"
                );

        if (!alreadyOpen) {
            Alert alert = new Alert(
                    zoneId, type, message, severity
            );

            generatedAlerts.add(repository.save(alert));
        }
    }

    @Transactional(readOnly = true)
    public List<Alert> getAlerts(String zoneId) {

        if (zoneId == null || zoneId.isBlank()) {
            return repository.findAllByOrderByCreatedAtDesc();
        }

        return repository.findByZoneIdOrderByCreatedAtDesc(zoneId);
    }

    @Transactional(readOnly = true)
    public Alert getAlertById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new AlertNotFoundException(
                                "Alerte introuvable : " + id
                        )
                );
    }

    @Transactional
    public Alert acknowledge(Long id) {

        Alert alert = getAlertById(id);
        alert.acknowledge();

        return repository.save(alert);
    }
}
