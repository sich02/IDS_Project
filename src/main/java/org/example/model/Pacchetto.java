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

    private double sconto; // Percentuale di sconto (es. 10.0 per il 10%)

    // COMPOSITE PATTERN:
    // Uso List<Prodotto> (la classe astratta) e non ProdottoSingolo.
    // Questo permette a un pacchetto di contenere anche altri pacchetti (nested composition).
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pacchetto_contenuto",
            joinColumns = @JoinColumn(name = "pacchetto_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti = new ArrayList<>();

    public Pacchetto(String nome, String descrizione, double sconto, Venditore venditore) {
        // Passiamo 0.0 come prezzo base al padre, perché verrà calcolato dinamicamente
        super(nome, descrizione, 0.0, venditore);
        this.sconto = sconto;
    }

    /**
     * Calcolo dinamico del prezzo (Composite Pattern).
     * Itera sui figli (prodotti), somma i loro prezzi e applica lo sconto.
     */
    @Override
    public double getPrezzo() {
        if (this.prodotti == null || this.prodotti.isEmpty()) {
            return 0.0;
        }

        // 1. Somma i prezzi dei componenti (ricorsivo se ci sono pacchetti dentro pacchetti)
        double somma = prodotti.stream()
                .mapToDouble(Prodotto::getPrezzo)
                .sum();

        // 2. Applica lo sconto
        if (sconto > 0) {
            return somma - (somma * (sconto / 100.0));
        }

        return somma;
    }
}