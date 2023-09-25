package com.curso.cleancode.branas.dto.ride;

import com.curso.cleancode.branas.constants.RideStatusEnum;
import com.curso.cleancode.branas.model.Ride;
import com.curso.cleancode.branas.utils.RideUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRideDTO {
    private String passengerId;
    private Double fromLat;
    private Double fromLong;
    private Double toLat;
    private Double toLong;

    public static Ride parseToEntity(RequestRideDTO rideDTO) {
        return Ride.builder()
            .rideId(UUID.randomUUID().toString())
            .passengerAccountId(rideDTO.passengerId)
            .rideStatus(RideStatusEnum.STATUS_REQUESTED.getRideStatus())
            .fare(BigDecimal.ZERO)
            .fromLat(rideDTO.fromLat)
            .fromLong(rideDTO.fromLong)
            .toLat(rideDTO.toLat)
            .toLong(rideDTO.toLong)
            .distance(RideUtils.calculateKilometerDistance(rideDTO.fromLat, rideDTO.fromLong, rideDTO.toLat, rideDTO.toLong))
            .date(new Date())
            .build();
    }
}
