package org.example.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public abstract class Venditore extends Utente {

    private String partitaIva;
    private String ragioneSociale;
    private String indirizzoSede;

    public Venditore(String nome, String cognome, String email, String password, RuoloUtente ruolo,
                     String partitaIva, String ragioneSociale, String indirizzoSede) {
        super(nome, cognome, email, password, ruolo);
        this.partitaIva = partitaIva;
        this.ragioneSociale = ragioneSociale;
        this.indirizzoSede = indirizzoSede;
    }
}
