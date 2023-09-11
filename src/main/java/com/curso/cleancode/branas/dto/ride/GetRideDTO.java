package com.curso.cleancode.branas.dto.ride;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRideDTO {
    @NotEmpty(message = "O campo \"rideId\" deve estar presente e não deve ser nulo")
    private String rideId;
}
