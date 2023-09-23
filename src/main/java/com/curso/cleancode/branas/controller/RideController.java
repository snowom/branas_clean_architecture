package com.curso.cleancode.branas.controller;

import com.curso.cleancode.branas.dto.ride.AcceptRideDTO;
import com.curso.cleancode.branas.dto.ride.GetRideDTO;
import com.curso.cleancode.branas.dto.ride.RequestRideDTO;
import com.curso.cleancode.branas.model.Ride;
import com.curso.cleancode.branas.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ride")
public class RideController {

    private final RideService rideService;

    RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/request")
    private ResponseEntity<?> requestRide(@Valid @RequestBody RequestRideDTO requestRideDTO) {
        String rideId = rideService.requestRide(requestRideDTO);
        Map<String, String> response = new HashMap<>();
        response.put("ride_id", rideId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/accept")
    @ResponseStatus(HttpStatus.OK)
    private void acceptRide(@Valid @RequestBody AcceptRideDTO acceptRideDTO) {
        rideService.acceptRide(acceptRideDTO);
    }

    @PostMapping("/getRide")
    private ResponseEntity<Ride> getRide(@Valid @RequestBody GetRideDTO getRideDTO) {
        Ride ride = rideService.getRide(getRideDTO.getRideId());
        return ResponseEntity.ok().body(ride);
    }
}
