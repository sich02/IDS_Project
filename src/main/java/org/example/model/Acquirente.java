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

    public Acquirente(String nome, String cognome, String email, String password){
        super(nome, cognome, email, password, RuoloUtente.ACQUIRENTE);
    }

}
