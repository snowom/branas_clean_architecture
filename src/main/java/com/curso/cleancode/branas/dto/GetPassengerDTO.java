package com.curso.cleancode.branas.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPassengerDTO {
    @NotEmpty(message = "O campo não pode estar vazio")
    @Length(min = 36, max = 36, message = "O campo deve conter 36 caracteres")
    private String account_id;
}
