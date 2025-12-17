package org.example.dto.response;

import org.example.model.Acquirente;

public record PartecipanteResponse(
        String nome,
        String cognome,
        String email
) {
    public static PartecipanteResponse fromEntity(Acquirente a){
        return new PartecipanteResponse(
                a.getNome(),
                a.getCognome(),
                a.getEmail()
        );
    }
}
