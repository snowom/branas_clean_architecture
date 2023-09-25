package com.curso.cleancode.branas.model;

import com.curso.cleancode.branas.constants.RideStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "ride")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    @Column(name = "ride_id")
    private String rideId;
    @Column(name = "passenger_id")
    private String passengerAccountId;
    @Column(name = "driver_id")
    private String driverAccountId;
    @Column(name = "status")
    private String rideStatus;
    @Column(name = "fare")
    private BigDecimal fare;
    @Column(name = "distance")
    private Float distance;
    @Column(name = "from_lat")
    private Double fromLat;
    @Column(name = "from_long")
    private Double fromLong;
    @Column(name = "to_lat")
    private Double toLat;
    @Column(name = "to_long")
    private Double toLong;
    @Column(name = "date")
    private Date date;
}
