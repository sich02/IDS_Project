package org.example.dto.response;

import org.example.model.Carrello;
import org.example.model.Prenotazione;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record CarrelloResponse(
        Long id,
        List<ProdottoResponse> prodotti,
        double totaleParziale
) {
    public static CarrelloResponse fromEntity(Carrello c) {
        List<ProdottoResponse> prodDTOs = c.getProdotti().stream().map(ProdottoResponse::fromEntity).toList();

        double totale = c.getProdotti().stream().mapToDouble(p -> p.getPrezzo()).sum();

        return new CarrelloResponse(c.getId(), prodDTOs, totale);
    }
}
