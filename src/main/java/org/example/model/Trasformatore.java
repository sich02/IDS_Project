package org.example.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Trasformatore extends Venditore{
    public Trasformatore(String nome, String cognome, String email, String password,
                         String partitaIva, String ragioneSociale, String indirizzoSede, String coordinate) {
        super(nome, cognome, email, password, RuoloUtente.TRASFORMATORE,
                partitaIva, ragioneSociale, indirizzoSede, coordinate);
    }
}
