package com.curso.cleancode.branas.service;

import com.curso.cleancode.branas.dto.position.UpdatePositionDTO;
import com.curso.cleancode.branas.exceptions.RideException;
import com.curso.cleancode.branas.model.Position;
import com.curso.cleancode.branas.model.Ride;
import com.curso.cleancode.branas.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final RideService rideService;

    PositionService(PositionRepository positionRepository, RideService rideService) {
        this.positionRepository = positionRepository;
        this.rideService = rideService;
    }

    public void updateRidePosition(UpdatePositionDTO positionDTO) {
        if (positionDTO.getLatitude() == null || positionDTO.getLongitude() == null) {
            throw new RideException("Falha ao atualizar localização. Valores de latitude ou longitude incorretos.");
        }
        Ride ride = this.rideService.getRide(positionDTO.getRideId());
        this.rideService.isAcceptedRide(ride);
        Position position = this.createPosition(positionDTO);
        positionRepository.save(position);
    }

    private Position createPosition(UpdatePositionDTO positionDTO) {
        return Position.builder()
            .positionId(UUID.randomUUID().toString())
            .rideId(positionDTO.getRideId())
            .latitude(positionDTO.getLatitude())
            .longitude(positionDTO.getLongitude())
            .date(new Date())
            .build();
    }
}
