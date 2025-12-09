package org.example.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)

public class Produttore extends Venditore{
    public Produttore(String nome, String cognome, String email, String password,
                      String partitaIva, String ragioneSociale, String indirizzoSede) {
        super(nome, cognome, email, password, RuoloUtente.PRODUTTORE, partitaIva, ragioneSociale, indirizzoSede);
    }
}

