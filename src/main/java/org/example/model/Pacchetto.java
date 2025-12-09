package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pacchetto extends Prodotto {
    private double sconto;

    // Aggregazione: Un pacchetto contiene prodotti singoli
    @ManyToMany
    private List<ProdottoSingolo> componenti = new ArrayList<>();

    public Pacchetto(String nome, String descrizione, double sconto, Venditore venditore) {
        // Il prezzo base può essere 0, viene calcolato dinamicamente dai componenti
        super(nome, descrizione, 0.0, venditore);
        this.sconto = sconto;
    }

    // Esempio di metodo del Composite: calcolo prezzo dinamico
    public double getPrezzoTotale() {
        double somma = componenti.stream().mapToDouble(Prodotto::getPrezzo).sum();
        return somma - (somma * sconto / 100);
    }
}