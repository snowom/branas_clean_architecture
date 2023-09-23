package com.curso.cleancode.branas.controller;

import com.curso.cleancode.branas.dto.user.CreateUserDTO;
import com.curso.cleancode.branas.dto.user.GetUserDTO;
import com.curso.cleancode.branas.model.User;
import com.curso.cleancode.branas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody CreateUserDTO createUserDTO, UriComponentsBuilder uriBuilder) {
        userService.createPassenger(createUserDTO);
        URI uri = uriBuilder.path("/getPassenger").build().toUri();
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Usuário criado com sucesso");
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping(path = "/getAccount")
    public ResponseEntity<User> getAccount(@Valid @RequestBody GetUserDTO getUserDTO) {
        String passengerDTOAccountId = getUserDTO.getAccount_id();
        User user = userService.getUser(passengerDTOAccountId);
        return ResponseEntity.ok().body(user);
    }
}
