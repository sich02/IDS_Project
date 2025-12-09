package org.example.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)

public class Acquirente extends Utente{
    private String indirizzoSpedizione;

    public Acquirente(String nome, String cognome, String email, String password, String indirizzoSpedizione) {
        super(nome, cognome, email, password, RuoloUtente.ACQUIRENTE);
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

}
