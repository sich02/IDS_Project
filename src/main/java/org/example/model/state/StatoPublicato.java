package org.example.model.state;

import org.example.model.Prodotto;

public class StatoPublicato implements StatoProdotto{

    @Override
    public void inviaInRevisione(Prodotto context){
        throw new IllegalStateException("Il prodotto è già stato pubblicato");
    }

    @Override
    public void approva(Prodotto context){
        throw new IllegalStateException("Il prodotto è già stato approvato");
    }

    @Override
    public void rifiuta(Prodotto context, String motivazione){
        throw  new IllegalStateException("Impossibile rifiutare un prodotto già pubblicato");
    }

    @Override
    public String toString(){
        return "Pubblicato";
    }
}
