package org.example.dto.request;

public record CreaEventoRequest(
        Long idAnimatore,
        String nome,
        String descrizione,
        String dataEvento,
        String luogo,
        int maxPartecipanti,
        double prezzoBiglietto
) {
}
