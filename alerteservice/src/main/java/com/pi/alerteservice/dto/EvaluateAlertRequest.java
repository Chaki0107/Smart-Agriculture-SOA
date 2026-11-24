package com.pi.alerteservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record EvaluateAlertRequest(

        @NotBlank
        String zoneId,

        @DecimalMin("0.0")
        @DecimalMax("100.0")
        Double soilMoisturePct,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("1.0")
        Double diseaseRisk

) {
}
