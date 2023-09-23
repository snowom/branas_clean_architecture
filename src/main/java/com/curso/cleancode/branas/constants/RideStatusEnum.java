package com.curso.cleancode.branas.constants;

import lombok.Getter;

public enum RideStatusEnum {

    STATUS_REQUESTED("requested"),
    STATUS_ACCEPTED("accepted"),
    STATUS_IN_PROGRESS("in_progress"),
    STATUS_COMPLETED("completed");

    @Getter
    private final String rideStatus;

    RideStatusEnum(String rideStatus) {
        this.rideStatus = rideStatus;
    }
}
