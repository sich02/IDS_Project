package org.example.factory;

import org.example.model.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UtenteFactory {

    public Utente creaUtente(RuoloUtente ruolo, Map<String, Object> dati) {
        // 1. Dati comuni a tutti
        String nome = (String) dati.get("nome");
        String cognome = (String) dati.get("cognome");
        String email = (String) dati.get("email");
        String password = (String) dati.get("password");

        // Variabili di appoggio
        String piva, ragSoc, sede;

        switch (ruolo) {
            // --- VENDITORI (Richiedono dati aziendali) ---
            case PRODUTTORE:
                piva = (String) dati.get("partitaIva");
                ragSoc = (String) dati.get("ragioneSociale");
                sede = (String) dati.get("indirizzoSede");
                return new Produttore(nome, cognome, email, password, piva, ragSoc, sede);

            case TRASFORMATORE:
                piva = (String) dati.get("partitaIva");
                ragSoc = (String) dati.get("ragioneSociale");
                sede = (String) dati.get("indirizzoSede");
                return new Trasformatore(nome, cognome, email, password, piva, ragSoc, sede);

            case DISTRIBUTORE:
                piva = (String) dati.get("partitaIva");
                ragSoc = (String) dati.get("ragioneSociale");
                sede = (String) dati.get("indirizzoSede");
                return new Distributore(nome, cognome, email, password, piva, ragSoc, sede);

            // --- ALTRI RUOLI (Senza dati aziendali) ---
            case ANIMATORE:
                return new Animatore(nome, cognome, email, password);

            case CURATORE:
                // Modifica: Tolta la matricola, ora è come un utente base
                return new Curatore(nome, cognome, email, password);

            case ACQUIRENTE:
                String indirizzoSpedizione = (String) dati.get("indirizzoSpedizione");
                return new Acquirente(nome, cognome, email, password, indirizzoSpedizione);

            default:
                throw new IllegalArgumentException("Ruolo non supportato: " + ruolo);
        }
    }
}