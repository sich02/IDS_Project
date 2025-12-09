package org.example.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProdottoSingolo extends Prodotto {
    // Eventuali campi specifici (es. boolean isBiologico, dataScadenza)

    public ProdottoSingolo(String nome, String descrizione, double prezzo, Venditore venditore) {
        super(nome, descrizione, prezzo, venditore);
    }
}