package org.example.dto.response;

import org.example.model.Prodotto;
import org.example.model.ProdottoSingolo;
import org.example.model.Pacchetto;

import java.util.List;
import java.util.Collections;
public record ProdottoResponse(
        Long id,
        String nome,
        String descrizione,
        Double prezzo,
        String stato,
        String nomeVenditore,
        String tipo,
        List<String> certificazioni
) {
    public static ProdottoResponse fromEntity(Prodotto p) {
        String tipo = (p instanceof Pacchetto) ? "PACCHETTO" : "SINGOLO";
        List<String> certs = Collections.emptyList();
        if(p instanceof ProdottoSingolo ps) {
            certs = ps.getCertificazioni().stream().map(c -> c.getTipo().toString()).toList();
        }
        String statoCorrente = p.getStatoNome();

        return new ProdottoResponse(
                p.getId(),
                p.getNome(),
                p.getDescrizione(),
                p.getPrezzo(),
                statoCorrente,
                p.getVenditore().getNome() + " " +p.getVenditore().getCognome(),
                tipo,
                certs
        );
    }
}
