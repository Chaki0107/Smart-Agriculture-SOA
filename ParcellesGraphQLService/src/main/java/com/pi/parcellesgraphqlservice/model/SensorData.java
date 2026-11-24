
package com.pi.parcellesgraphqlservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "field_zone")
    private String fieldZone;

    @Column(name = "crop_type")
    private String cropType;

    @Column(name = "growth_stage")
    private String growthStage;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "irrigation_status")
    private String irrigationStatus;

    @Column(name = "irrigation_amount_l")
    private Double irrigationAmountL;

    @Column(name = "water_flow_rate_l_min")
    private Double waterFlowRateLMin;

    public SensorData() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFieldZone() {
        return fieldZone;
    }

    public String getCropType() {
        return cropType;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getIrrigationStatus() {
        return irrigationStatus;
    }

    public Double getIrrigationAmountL() {
        return irrigationAmountL;
    }

    public Double getWaterFlowRateLMin() {
        return waterFlowRateLMin;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setFieldZone(String fieldZone) {
        this.fieldZone = fieldZone;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setIrrigationStatus(String irrigationStatus) {
        this.irrigationStatus = irrigationStatus;
    }

    public void setIrrigationAmountL(Double irrigationAmountL) {
        this.irrigationAmountL = irrigationAmountL;
    }

    public void setWaterFlowRateLMin(Double waterFlowRateLMin) {
        this.waterFlowRateLMin = waterFlowRateLMin;
    }
}
