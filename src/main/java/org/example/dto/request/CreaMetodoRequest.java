package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreaMetodoRequest(
        @NotBlank String nome,
        @NotBlank String descrizione
) {
}
