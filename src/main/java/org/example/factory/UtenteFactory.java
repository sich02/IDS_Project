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
        String coordinate = request.coordinate();

        String piva, ragSoc, sede;

        if (isVenditore(request.ruolo())){
            if(coordinate == null || coordinate.trim().isEmpty()){
                throw new IllegalArgumentException("Le coordinate sono obbligatorie");
            }
        }

        switch (ruolo) {
            case PRODUTTORE:
                return new Produttore(nome, cognome, email, password,request.partitaIva(),
                                        request.ragioneSociale(), request.indirizzoSede(), coordinate);

            case TRASFORMATORE:
                return new Trasformatore(nome, cognome, email, password,request.partitaIva(),
                        request.ragioneSociale(), request.indirizzoSede(), coordinate);

            case DISTRIBUTORE:

                return new Distributore(nome, cognome, email, password,request.partitaIva(),
                        request.ragioneSociale(), request.indirizzoSede(), coordinate);

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

    private boolean isVenditore(RuoloUtente ruolo) {
        return ruolo == RuoloUtente.PRODUTTORE ||
               ruolo == RuoloUtente.TRASFORMATORE ||
               ruolo == RuoloUtente.DISTRIBUTORE;
    }
}