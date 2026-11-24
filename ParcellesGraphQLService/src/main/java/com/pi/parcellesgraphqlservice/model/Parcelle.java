package com.pi.parcellesgraphqlservice.model;

import java.util.List;

public record Parcelle(
        String zoneId,
        List<String> cultures,
        Double latitude,
        Double longitude,
        String stadeCroissance
) {
}
