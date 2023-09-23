package com.curso.cleancode.branas.controller;

import com.curso.cleancode.branas.dto.position.UpdatePositionDTO;
import com.curso.cleancode.branas.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "/position")
public class PositionController {

    private final PositionService positionService;

    PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/updateRide")
    @ResponseStatus(HttpStatus.OK)
    private void updateRidePosition(@RequestBody UpdatePositionDTO updatePositionDTO) {
        this.positionService.updateRidePosition(updatePositionDTO);
    }
}
