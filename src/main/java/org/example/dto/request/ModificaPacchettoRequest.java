package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModificaPacchettoRequest(
        @NotNull Long idPacchetto,
        @NotNull Long idDistributore,
        @NotBlank String nome,
        @NotBlank String descrizione,
        double sconto,
        List<Long> idsProdottoDaIncludere
) {
}
