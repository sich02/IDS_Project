package org.example.dto.request;

import java.util.List;

public record CreaPacchettoRequest(
        Long idDistributore,
        String nome,
        String descrizione,
        int quantita,
        List<Long> idsProdottoDaIncludere
) {
}
