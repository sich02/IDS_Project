package org.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Campo obbligatorio")
        @Email(message = "Formato email non valido")
        String email,
        @NotBlank(message = "Campo obbligatorio")
        String password
) {
}
