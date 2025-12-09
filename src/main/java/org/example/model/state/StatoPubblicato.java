package org.example.model.state;

import org.example.model.Contenuto;

public class StatoPubblicato implements StatoContenuto {

    @Override
    public void inviaInRevisione(Contenuto context) {
        throw new IllegalStateException("Errore: Il contenuto è già pubblicato.");
    }

    @Override
    public void approva(Contenuto context) {
        throw new IllegalStateException("Errore: Il contenuto è già stato approvato.");
    }

    @Override
    public void rifiuta(Contenuto context, String motivazione) {
        // Opzionale: permettere di ritirare un contenuto pubblicato?
        // Per ora lo blocchiamo come da specifiche base.
        throw new IllegalStateException("Errore: Impossibile rifiutare un contenuto già pubblicato. Va prima sospeso.");
    }

    @Override
    public String toString() {
        return "PUBBLICATO";
    }
}