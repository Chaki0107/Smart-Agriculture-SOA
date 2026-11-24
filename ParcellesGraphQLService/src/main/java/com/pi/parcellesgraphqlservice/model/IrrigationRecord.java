package com.pi.parcellesgraphqlservice.model;

public record IrrigationRecord(
        String id,
        String zoneId,
        String timestamp,
        String irrigationStatus,
        Double irrigationAmountL,
        Double waterFlowRateLMin
) {
}
