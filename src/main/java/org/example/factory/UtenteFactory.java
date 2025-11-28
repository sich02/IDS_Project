package org.example.factory;
import org.example.model.*;
import java.util.Map;
public class UtenteFactory {
    public Utente creaUtente(RuoloUtente ruolo, Map<String, Object> dati) {
        String nome = (String) dati.get("nome");
        String cognome = (String) dati.get("cognome");
        String email = (String) dati.get("email");
        String password = (String) dati.get("password");

        switch (ruolo) {
            case PRODUTTORE:
                return new Produttore(nome, cognome, email, password,
                        (String) dati.get("partitaIva"),
                        (String) dati.get("ragioneSociale"),
                        (String) dati.get("indirizzoSede"));
            case CURATORE:
                return new Curatore(nome, cognome, email, password,
                        (String) dati.get("matricola"));
            case ACQUIRENTE:
                // Aggiungi logica se necessario
                return new Acquirente(nome, cognome, email, password,
                        (String) dati.get("indirizzoSpedizione"));
            default:
                throw new IllegalArgumentException("Ruolo non supportato: " + ruolo);
        }
    }
}
