package org.example.model.state;

import org.example.model.Contenuto;

public class StatoBozza implements StatoContenuto {

    @Override
    public void inviaInRevisione(Contenuto context) {
        // Transizione valida: Bozza -> In Approvazione
        context.setStato(new StatoInApprovazione());
    }

    @Override
    public void approva(Contenuto context) {
        throw new IllegalStateException("Errore: Non puoi approvare un contenuto che è ancora in BOZZA. Deve essere prima inviato.");
    }

    @Override
    public void rifiuta(Contenuto context, String motivazione) {
        throw new IllegalStateException("Errore: Non puoi rifiutare una BOZZA.");
    }

    @Override
    public String toString() {
        return "BOZZA"; // Fondamentale per il salvataggio nel DB
    }
}