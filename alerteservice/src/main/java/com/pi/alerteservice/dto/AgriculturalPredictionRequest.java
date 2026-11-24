package com.pi.alerteservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AgriculturalPredictionRequest(
        @JsonProperty("crop_type")
        String cropType,

        @JsonProperty("growth_stage")
        String growthStage,

        @JsonProperty("field_zone")
        String fieldZone,

        @JsonProperty("air_temperature_c")
        Double airTemperatureC,

        @JsonProperty("relative_humidity_pct")
        Double relativeHumidityPct,

        @JsonProperty("soil_temperature_c")
        Double soilTemperatureC,

        @JsonProperty("soil_moisture_pct")
        Double soilMoisturePct,

        @JsonProperty("soil_ph")
        Double soilPh,

        @JsonProperty("soil_ec_ds_m")
        Double soilEcDsM,

        @JsonProperty("nitrogen_mg_kg")
        Double nitrogenMgKg,

        @JsonProperty("phosphorus_mg_kg")
        Double phosphorusMgKg,

        @JsonProperty("potassium_mg_kg")
        Double potassiumMgKg,

        @JsonProperty("rainfall_mm")
        Double rainfallMm,

        @JsonProperty("solar_radiation_w_m2")
        Double solarRadiationWM2,

        @JsonProperty("wind_speed_m_s")
        Double windSpeedMS,

        @JsonProperty("leaf_wetness_pct")
        Double leafWetnessPct,

        @JsonProperty("evapotranspiration_mm")
        Double evapotranspirationMm
) {
}