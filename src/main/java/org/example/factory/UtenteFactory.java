package org.example.factory;

import org.example.dto.request.RegistrazioneRequest;
import org.example.model.*;
import org.springframework.stereotype.Component;

@Component
public class UtenteFactory {

    public Utente creaUtente(RegistrazioneRequest request) {
        String nome = request.nome();
        String cognome = request.cognome();
        String email = request.email();
        String password = request.password();
        RuoloUtente ruolo = request.ruolo();

        String piva, ragSoc, sede;

        switch (ruolo) {
            case PRODUTTORE:
                return new Produttore(nome, cognome, email, password,request.partitaIva(),
                                        request.ragioneSociale(), request.indirizzoSede());

            case TRASFORMATORE:
                return new Trasformatore(nome, cognome, email, password,request.partitaIva(),
                        request.ragioneSociale(), request.indirizzoSede());

            case DISTRIBUTORE:

                return new Distributore(nome, cognome, email, password,request.partitaIva(),
                        request.ragioneSociale(), request.indirizzoSede());

            case ANIMATORE:
                return new Animatore(nome, cognome, email, password);

            case CURATORE:
                return new Curatore(nome, cognome, email, password);

            case ACQUIRENTE:
                return new Acquirente(nome, cognome, email, password, request.indirizzoSpedizione());

            default:
                throw new IllegalArgumentException("Ruolo non supportato: " + ruolo);
        }
    }
}