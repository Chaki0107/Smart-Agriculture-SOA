package com.pi.iotwebservice;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "sensor_node_id")
    private String sensorNodeId;

    @Column(name = "field_zone")
    private String fieldZone;

    private Double latitude;
    private Double longitude;

    @Column(name = "crop_type")
    private String cropType;

    @Column(name = "growth_stage")
    private String growthStage;

    @Column(name = "air_temperature_c")
    private Double airTemperatureC;

    @Column(name = "relative_humidity_pct")
    private Double relativeHumidityPct;

    @Column(name = "soil_temperature_c")
    private Double soilTemperatureC;

    @Column(name = "soil_moisture_pct")
    private Double soilMoisturePct;

    @Column(name = "soil_ph")
    private Double soilPh;

    @Column(name = "soil_ec_ds_m")
    private Double soilEcDsM;

    @Column(name = "nitrogen_mg_kg")
    private Double nitrogenMgKg;

    @Column(name = "phosphorus_mg_kg")
    private Double phosphorusMgKg;

    @Column(name = "potassium_mg_kg")
    private Double potassiumMgKg;

    @Column(name = "atmospheric_pressure_hpa")
    private Double atmosphericPressureHpa;

    @Column(name = "solar_radiation_w_m2")
    private Double solarRadiationWM2;

    @Column(name = "light_intensity_lux")
    private Double lightIntensityLux;

    @Column(name = "wind_speed_m_s")
    private Double windSpeedMS;

    @Column(name = "wind_direction_deg")
    private Double windDirectionDeg;

    @Column(name = "rainfall_mm")
    private Double rainfallMm;

    @Column(name = "leaf_wetness_pct")
    private Double leafWetnessPct;

    @Column(name = "co2_ppm")
    private Double co2Ppm;

    @Column(name = "evapotranspiration_mm")
    private Double evapotranspirationMm;

    @Column(name = "irrigation_status")
    private String irrigationStatus;

    @Column(name = "irrigation_amount_l")
    private Double irrigationAmountL;

    @Column(name = "water_flow_rate_l_min")
    private Double waterFlowRateLMin;

    @Column(name = "battery_level_pct")
    private Double batteryLevelPct;

    @Column(name = "signal_strength_dbm")
    private Double signalStrengthDbm;

    @Column(name = "sensor_status")
    private String sensorStatus;

    @Column(name = "environmental_condition")
    private String environmentalCondition;

    @Column(name = "yield_t_ha")
    private Double yieldTHa;

    @Column(name = "disease_risk")
    private Double diseaseRisk;

    public SensorReading() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensorNodeId() {
        return sensorNodeId;
    }

    public void setSensorNodeId(String sensorNodeId) {
        this.sensorNodeId = sensorNodeId;
    }

    public String getFieldZone() {
        return fieldZone;
    }

    public void setFieldZone(String fieldZone) {
        this.fieldZone = fieldZone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public Double getAirTemperatureC() {
        return airTemperatureC;
    }

    public void setAirTemperatureC(Double airTemperatureC) {
        this.airTemperatureC = airTemperatureC;
    }

    public Double getRelativeHumidityPct() {
        return relativeHumidityPct;
    }

    public void setRelativeHumidityPct(Double relativeHumidityPct) {
        this.relativeHumidityPct = relativeHumidityPct;
    }

    public Double getSoilTemperatureC() {
        return soilTemperatureC;
    }

    public void setSoilTemperatureC(Double soilTemperatureC) {
        this.soilTemperatureC = soilTemperatureC;
    }

    public Double getSoilMoisturePct() {
        return soilMoisturePct;
    }

    public void setSoilMoisturePct(Double soilMoisturePct) {
        this.soilMoisturePct = soilMoisturePct;
    }

    public Double getSoilPh() {
        return soilPh;
    }

    public void setSoilPh(Double soilPh) {
        this.soilPh = soilPh;
    }

    public Double getSoilEcDsM() {
        return soilEcDsM;
    }

    public void setSoilEcDsM(Double soilEcDsM) {
        this.soilEcDsM = soilEcDsM;
    }

    public Double getNitrogenMgKg() {
        return nitrogenMgKg;
    }

    public void setNitrogenMgKg(Double nitrogenMgKg) {
        this.nitrogenMgKg = nitrogenMgKg;
    }

    public Double getPhosphorusMgKg() {
        return phosphorusMgKg;
    }

    public void setPhosphorusMgKg(Double phosphorusMgKg) {
        this.phosphorusMgKg = phosphorusMgKg;
    }

    public Double getPotassiumMgKg() {
        return potassiumMgKg;
    }

    public void setPotassiumMgKg(Double potassiumMgKg) {
        this.potassiumMgKg = potassiumMgKg;
    }

    public Double getAtmosphericPressureHpa() {
        return atmosphericPressureHpa;
    }

    public void setAtmosphericPressureHpa(Double atmosphericPressureHpa) {
        this.atmosphericPressureHpa = atmosphericPressureHpa;
    }

    public Double getSolarRadiationWM2() {
        return solarRadiationWM2;
    }

    public void setSolarRadiationWM2(Double solarRadiationWM2) {
        this.solarRadiationWM2 = solarRadiationWM2;
    }

    public Double getLightIntensityLux() {
        return lightIntensityLux;
    }

    public void setLightIntensityLux(Double lightIntensityLux) {
        this.lightIntensityLux = lightIntensityLux;
    }

    public Double getWindSpeedMS() {
        return windSpeedMS;
    }

    public void setWindSpeedMS(Double windSpeedMS) {
        this.windSpeedMS = windSpeedMS;
    }

    public Double getWindDirectionDeg() {
        return windDirectionDeg;
    }

    public void setWindDirectionDeg(Double windDirectionDeg) {
        this.windDirectionDeg = windDirectionDeg;
    }

    public Double getRainfallMm() {
        return rainfallMm;
    }

    public void setRainfallMm(Double rainfallMm) {
        this.rainfallMm = rainfallMm;
    }

    public Double getLeafWetnessPct() {
        return leafWetnessPct;
    }

    public void setLeafWetnessPct(Double leafWetnessPct) {
        this.leafWetnessPct = leafWetnessPct;
    }

    public Double getCo2Ppm() {
        return co2Ppm;
    }

    public void setCo2Ppm(Double co2Ppm) {
        this.co2Ppm = co2Ppm;
    }

    public Double getEvapotranspirationMm() {
        return evapotranspirationMm;
    }

    public void setEvapotranspirationMm(Double evapotranspirationMm) {
        this.evapotranspirationMm = evapotranspirationMm;
    }

    public String getIrrigationStatus() {
        return irrigationStatus;
    }

    public void setIrrigationStatus(String irrigationStatus) {
        this.irrigationStatus = irrigationStatus;
    }

    public Double getIrrigationAmountL() {
        return irrigationAmountL;
    }

    public void setIrrigationAmountL(Double irrigationAmountL) {
        this.irrigationAmountL = irrigationAmountL;
    }

    public Double getWaterFlowRateLMin() {
        return waterFlowRateLMin;
    }

    public void setWaterFlowRateLMin(Double waterFlowRateLMin) {
        this.waterFlowRateLMin = waterFlowRateLMin;
    }

    public Double getBatteryLevelPct() {
        return batteryLevelPct;
    }

    public void setBatteryLevelPct(Double batteryLevelPct) {
        this.batteryLevelPct = batteryLevelPct;
    }

    public Double getSignalStrengthDbm() {
        return signalStrengthDbm;
    }

    public void setSignalStrengthDbm(Double signalStrengthDbm) {
        this.signalStrengthDbm = signalStrengthDbm;
    }

    public String getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public String getEnvironmentalCondition() {
        return environmentalCondition;
    }

    public void setEnvironmentalCondition(String environmentalCondition) {
        this.environmentalCondition = environmentalCondition;
    }

    public Double getYieldTHa() {
        return yieldTHa;
    }

    public void setYieldTHa(Double yieldTHa) {
        this.yieldTHa = yieldTHa;
    }

    public Double getDiseaseRisk() {
        return diseaseRisk;
    }

    public void setDiseaseRisk(Double diseaseRisk) {
        this.diseaseRisk = diseaseRisk;
    }
}