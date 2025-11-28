package org.example.model.state;

import org.example.model.Prodotto;

public interface StatoProdotto {
    void inviaInRevisione(Prodotto context);
    void approva(Prodotto context);
    void rifiuta(Prodotto context, String motivazione);
}
