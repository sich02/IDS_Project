package org.example.dto.request;

import org.example.model.RuoloUtente;
public record RegistrazioneRequest(
        String nome,
        String cognome,
        String email,
        String password,
        RuoloUtente ruolo,

        String partitaIva,
        String ragioneSociale,
        String indirizzoSede,

        String indirizzoSpedizione
) {
}
