package org.example.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MetodoTrasformazione extends Contenuto {
    public MetodoTrasformazione(String nome, String descrizione) {
        super(nome, descrizione);
    }
}
