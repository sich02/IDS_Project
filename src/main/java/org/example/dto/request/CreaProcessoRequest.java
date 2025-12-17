package org.example.dto.request;

import java.util.List;

public record CreaProcessoRequest(
        Long idTrasformatore,
        List<Integer> idsProdottoInput,
        String nomeOutput,
        String descrizioneOutput,
        double prezzoOutput,
        String descrizioneProcesso,
        List<CertificazioneRequest> certificazioni
) {
}
