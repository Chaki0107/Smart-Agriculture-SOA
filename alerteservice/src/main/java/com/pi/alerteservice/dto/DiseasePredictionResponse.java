package com.pi.alerteservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DiseasePredictionResponse(
        String model,
        String target,

        @JsonProperty("predicted_class")
        Integer predictedClass,

        @JsonProperty("class_1_probability")
        Double class1Probability
) {
}