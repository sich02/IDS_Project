package org.example.dto.request;

import java.util.List;

public record CreaProdottoRequest(
        Long idProduttore,
        String nome,
        String descrizione,
        double prezzo,
        int quantita,
        List<CertificazioneRequest> certificazioni
) {
}
