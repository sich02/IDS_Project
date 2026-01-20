package org.example.factory;

import org.example.dto.request.RegistrazioneRequest;
import org.example.model.*;
import org.springframework.stereotype.Component;

@Component
public class UtenteFactory {

    public Utente creaUtente(RegistrazioneRequest request) {

        if (isVenditore(request.ruolo())){
            if(request.coordinate() == null || request.coordinate().trim().isEmpty()){
                throw new IllegalArgumentException("Le coordinate sono obbligatorie");
            }
        }

        switch (request.ruolo()) {
            case PRODUTTORE:
                return new Produttore(request.nome(), request.cognome(), request.email(),
                        request.password(), request.partitaIva(),
                        request.ragioneSociale(), request.indirizzoSede(), request.coordinate());

            case TRASFORMATORE:
                return new Trasformatore(request.nome(), request.cognome(), request.email(), request.password(),
                        request.partitaIva(), request.ragioneSociale(), request.indirizzoSede(), request.coordinate());

            case DISTRIBUTORE:
                return new Distributore(request.nome(), request.cognome(), request.email(), request.password(),
                        request.partitaIva(), request.ragioneSociale(), request.indirizzoSede(), request.coordinate());

            case ANIMATORE:
                return new Animatore(request.nome(), request.cognome(), request.email(), request.password());

            case CURATORE:
                return new Curatore(request.nome(), request.cognome(), request.email(), request.password());

            case ACQUIRENTE:
                return new Acquirente(request.nome(), request.cognome(), request.email(), request.password(),
                        request.indirizzoSpedizione());

            case GESTORE_PIATTAFORMA:
                return new Gestore(request.nome(), request.cognome(), request.email(), request.password());

            default:
                throw new IllegalArgumentException("Ruolo non supportato: " + request.ruolo());
        }
    }

    private boolean isVenditore(RuoloUtente ruolo) {
        return ruolo == RuoloUtente.PRODUTTORE ||
               ruolo == RuoloUtente.TRASFORMATORE ||
               ruolo == RuoloUtente.DISTRIBUTORE;
    }
}