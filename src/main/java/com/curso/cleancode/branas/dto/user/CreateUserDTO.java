package com.curso.cleancode.branas.dto.user;

import com.curso.cleancode.branas.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CreateUserDTO {
    @NotEmpty(message = "O campo nome não pode estar vazio")
    @Length(min = 10, max = 30, message = "O campo \"nome\" deve conter entre 10 e 30 caracteres")
    private String nome;
    @NotEmpty(message = "O campo email deve estar presente e não deve ser nulo")
    private String email;
    @NotEmpty(message = "O campo cpf deve estar presente e não deve ser nulo")
    private String cpf;
    @NotNull(message = "O campo isPassenger deve estar presente e não deve ser nulo")
    private Boolean isPassenger;

    public static User parseToEntity(CreateUserDTO createUserDTO) {
        return User.builder()
            .accountId(UUID.randomUUID().toString())
            .nome(createUserDTO.nome)
            .email(createUserDTO.email)
            .cpf(createUserDTO.cpf)
            .isPassenger(createUserDTO.isPassenger)
            .build();
    }
}