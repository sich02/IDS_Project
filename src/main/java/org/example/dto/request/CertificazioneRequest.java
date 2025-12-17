package org.example.dto.request;

public record CertificazioneRequest(
        String nome,
        String enteRilascio,
        String descrizione
) {
}
