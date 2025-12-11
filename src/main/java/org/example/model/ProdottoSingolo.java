package org.example.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProdottoSingolo extends Prodotto {

    @OneToMany(cascade = CascadeType.ALL)
    private List<Certificazione> certificazioni = new ArrayList<>();

    public ProdottoSingolo(String nome, String descrizione, double prezzo, Venditore venditore) {
        super(nome, descrizione, prezzo, venditore);
    }

    public void aggiungiCertificazione(Certificazione c) {
        this.certificazioni.add(c);
    }
}