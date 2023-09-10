package com.curso.cleancode.branas.dto;

import com.curso.cleancode.branas.model.Passenger;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CreatePassengerDTO {
    @NotEmpty(message = "O campo nome não pode estar vazio")
    @Length(min = 10, max = 30, message = "O campo \"nome\" deve conter entre 10 e 30 caracteres")
    private String nome;
    @NotEmpty(message = "O campo email deve estar presente")
    private String email;
    private String cpf;

    public static Passenger parseToEntity(CreatePassengerDTO createPassengerDTO) {
        return Passenger.builder()
            .accountId(UUID.randomUUID().toString())
            .nome(createPassengerDTO.nome)
            .email(createPassengerDTO.email)
            .cpf(createPassengerDTO.cpf)
            .build();
    }
}