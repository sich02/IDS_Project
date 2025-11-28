package org.example.model.state;

import org.example.model.Prodotto;

public class StatoBozza implements StatoProdotto{
    @Override
    public void inviaInRevisione(Prodotto context) {
        context.setStato(new StatoInApprovazione());
    }

    @Override
    public void approva(Prodotto context) {
        throw new IllegalStateException("Non puoi approvare una bozza senza inviarla");
    }

    @Override
    public void rifiuta(Prodotto context, String motivazione){
        throw new IllegalStateException("Non puoi rifiutare una bozza");
    }

    @Override
    public String toString() {
        return "BOZZA";
    }
}
