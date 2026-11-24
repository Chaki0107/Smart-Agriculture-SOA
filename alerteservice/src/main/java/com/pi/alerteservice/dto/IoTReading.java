package com.pi.alerteservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IoTReading(
        Long id,
        String timestamp,
        String fieldZone,
        String cropType,
        String growthStage,
        Double soilMoisturePct,
        Double diseaseRisk,
        Double airTemperatureC,
        Double relativeHumidityPct,
        Double soilTemperatureC,
        Double soilPh,
        Double soilEcDsM,
        Double nitrogenMgKg,
        Double phosphorusMgKg,
        Double potassiumMgKg,
        Double rainfallMm,
        Double solarRadiationWM2,
        Double windSpeedMS,
        Double leafWetnessPct,
        Double evapotranspirationMm
) {

}