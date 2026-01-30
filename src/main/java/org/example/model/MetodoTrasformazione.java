package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MetodoTrasformazione extends Contenuto {

    @ManyToOne
    private Trasformatore autore;

    public MetodoTrasformazione(String nome, String descrizione, Trasformatore autore) {
        super(nome, descrizione);
        this.autore = autore;
    }
}
