package org.example.model.state;
import org.example.model.Contenuto;

public interface StatoContenuto {
    void inviaInRevisione(Contenuto context);
    void approva(Contenuto context);
    void rifiuta(Contenuto context, String motivazione);
}
