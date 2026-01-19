package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ModificaEventoRequest(
        @NotNull Long idEvento,
        @NotNull Long idAnimatore,
        @NotBlank String nome,
        @NotBlank String descrizione,
        String dataEvento,
        @NotBlank String luogo,
        @Positive int maxPartecipanti,
        @Positive double prezzoBiglietto
) {
}
