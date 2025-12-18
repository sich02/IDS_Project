package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ModificaProdottoSingoloRequest(
        @NotNull Long idProdotto,
        @NotNull Long idVenditore,
        @NotBlank String nome,
        @NotBlank String descrizione,
        @Positive double prezzo,
        List<CertificazioneRequest> certificazioni
) {
}
