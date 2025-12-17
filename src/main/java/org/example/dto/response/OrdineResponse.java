package org.example.dto.response;

import org.example.model.Ordine;

import java.util.List;
import java.util.stream.Collectors;

public record OrdineResponse(
        Long id,
        String dataCreazione,
        double totale,
        String stato,
        List<String> nomiProdotti //è una lista semplificata dei prodotti
) {
   public static OrdineResponse fromEntity(Ordine o) {
       List<String> prodotti = o.getProdotti().stream().map(p->p.getNome() + " (" + p.getPrezzo() + "€)")
               .toList();
       return new OrdineResponse(
               o.getId(),
               o.getDataCreazione().toString(),
               o.getTotale(),
               o.getStato(),
               prodotti

       );
   }
}
