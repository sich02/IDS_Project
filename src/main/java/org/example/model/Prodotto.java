package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Prodotto extends Contenuto {

    private double prezzo;
    private int quantitaDisponibile;


    @ManyToOne
    @JoinColumn(name = "venditore_id")
    private Venditore venditore;


    public Prodotto(String nome, String descrizione, double prezzo, int quantitaDisponibile, Venditore venditore) {
        super(nome, descrizione);
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.venditore = venditore;
    }
}