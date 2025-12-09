package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED) // Per ProdottoSingolo e Pacchetto
public abstract class Prodotto extends Contenuto {

    private double prezzo;

    // Associazione con chi lo vende (Produttore, Trasformatore o Distributore)
    @ManyToOne
    @JoinColumn(name = "venditore_id")
    private Venditore venditore;

    // Costruttore
    public Prodotto(String nome, String descrizione, double prezzo, Venditore venditore) {
        super(nome, descrizione); // Passa i dati comuni al padre Contenuto
        this.prezzo = prezzo;
        this.venditore = venditore;
    }
}