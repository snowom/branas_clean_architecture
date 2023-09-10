package com.curso.cleancode.branas.controller;

import com.curso.cleancode.branas.dto.CreatePassengerDTO;
import com.curso.cleancode.branas.dto.GetPassengerDTO;
import com.curso.cleancode.branas.model.Passenger;
import com.curso.cleancode.branas.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/passenger")
public class PassageiroController {

    @Autowired
    PassengerService passengerService;

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody CreatePassengerDTO createPassengerDTO, UriComponentsBuilder uriBuilder) {
        passengerService.createPassenger(createPassengerDTO);
        URI uri = uriBuilder.path("/getPassenger").build().toUri();
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Passageiro criado com sucesso");
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping(path = "/getAccount")
    public ResponseEntity<Passenger> getAccount(@Valid @RequestBody GetPassengerDTO getPassengerDTO) {
        String passengerDTOAccountId = getPassengerDTO.getAccount_id();
        Passenger passenger = passengerService.getPassenger(passengerDTOAccountId);
        return ResponseEntity.ok().body(passenger);
    }
}
