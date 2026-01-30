package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreaMetodoRequest(
        @NotNull Long idTrasformatore,
        @NotBlank String nome,
        @NotBlank String descrizione
) {
}
