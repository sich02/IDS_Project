package org.example.dto.response;

import org.example.model.RuoloUtente;
import org.example.model.Utente;

public record UtenteResponse(
        Long id,
        String nome,
        String cognome,
        String email,
        RuoloUtente ruolo
) {
    public static UtenteResponse fromEntity(Utente u) {
        return new UtenteResponse(u.getId(), u.getNome(), u.getCognome(), u.getEmail(), u.getRuolo());
    }
}
