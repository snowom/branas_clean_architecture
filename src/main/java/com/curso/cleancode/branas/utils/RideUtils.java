package com.curso.cleancode.branas.utils;

public final class RideUtils {

    private RideUtils() {}

    public static Float calculateKilometerDistance(Double fromLat, Double fromLong, Double toLat, Double toLong) {
        double theta = fromLong - toLong;
        double distance = 60 * 1.1515 * (180/Math.PI) * Math.acos(
            Math.sin(fromLat * (Math.PI/180)) * Math.sin(toLat * (Math.PI/180)) +
            Math.cos(fromLat * (Math.PI/180)) * Math.cos(toLat * (Math.PI/180)) * Math.cos(theta * (Math.PI/180))
        );
        return (float) Math.round(distance * 1.609344);
    }
}
