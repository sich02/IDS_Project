package org.example.model.state;

import org.example.model.Prodotto;

public class StatoInApprovazione implements StatoProdotto {
    @Override
    public void inviaInRevisione(Prodotto context) {
        throw new IllegalStateException("IL PRODOTTO E' GIA' IN ATTESA DI REVISIONE");
    }
    @Override
    public void approva(Prodotto context) {
        context.setStato(new StatoPubblicato());
    }

    @Override
    public void rifiuta(Prodotto context, String motivazione){
        //transizione valida (RIFIUTO-> torna bozza)
        context.setStato(new StatoBozza());
    }

    @Override
    public String toString() {
        return "IN_APPROVAZIONE";
    }
}
