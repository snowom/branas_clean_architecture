package com.curso.cleancode.branas;

import com.curso.cleancode.branas.constants.RideStatusEnum;
import com.curso.cleancode.branas.dto.position.UpdatePositionDTO;
import com.curso.cleancode.branas.exceptions.RideException;
import com.curso.cleancode.branas.model.Position;
import com.curso.cleancode.branas.model.Ride;
import com.curso.cleancode.branas.repository.PositionRepository;
import com.curso.cleancode.branas.service.PositionService;
import com.curso.cleancode.branas.service.RideService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

public class PositionServiceTests extends MockitoExtension {

    @Mock
    PositionRepository positionRepository;
    @Mock
    RideService rideService;
    @InjectMocks
    PositionService positionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void update_ride_position_success() {
        UpdatePositionDTO positionDTO = new UpdatePositionDTO("12345", 12.345, -45.973);
        Date currentDate = new Date();
        Position responsePosition = Position.builder().positionId("12345").rideId("12345").longitude(1234.3).latitude(1234.5).date(currentDate).build();
        Mockito.when(rideService.getRide(positionDTO.getRideId())).thenReturn(
            Ride.builder().rideStatus(RideStatusEnum.STATUS_ACCEPTED.getRideStatus()).build()
        );
        Mockito.when(positionRepository.save(Mockito.any(Position.class))).thenReturn(responsePosition);
        positionService.updateRidePosition(positionDTO);
        Assertions.assertEquals(responsePosition.getDate(), currentDate);
    }

    @Test
    public void update_ride_position_fail_null_lat_or_long() {
        RideException exception = Assertions.assertThrows(RideException.class, () -> {
            UpdatePositionDTO positionDTO = new UpdatePositionDTO("12345", null, null);
            positionService.updateRidePosition(positionDTO);
        });
        Assertions.assertEquals("Falha ao atualizar localização. Valores de latitude ou longitude incorretos.", exception.getMessage());
    }
}
