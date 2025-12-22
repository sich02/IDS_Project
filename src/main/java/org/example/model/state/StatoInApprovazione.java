package org.example.model.state;

import org.example.model.Contenuto;

public class StatoInApprovazione implements StatoContenuto {

    @Override
    public void inviaInRevisione(Contenuto context) {
        throw new IllegalStateException("Errore: Il contenuto è GIA' in attesa di revisione.");
    }

    @Override
    public void approva(Contenuto context) {
        context.setStato(new StatoPubblicato());
    }

    @Override
    public void rifiuta(Contenuto context, String motivazione) {
        System.out.println("Contenuto rifiutato per: " + motivazione);
        context.setStato(new StatoBozza());
    }

    @Override
    public String toString() {
        return "IN_APPROVAZIONE";
    }
}