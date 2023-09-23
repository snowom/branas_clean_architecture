package com.curso.cleancode.branas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "position")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    @Id
    @Column(name = "position_id")
    private String positionId;
    @Column(name = "ride_id")
    private String rideId;
    @Column(name = "lat")
    private Double latitude;
    @Column(name = "long")
    private Double longitude;
    @Column(name = "date")
    private Date date;
}
