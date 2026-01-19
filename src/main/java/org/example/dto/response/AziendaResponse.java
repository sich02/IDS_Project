package org.example.dto.response;

import org.example.model.Venditore;

public record AziendaResponse(
        Long id,
        String ragioneSociale,
        String coordinate,
        String tipo
) {
    public static AziendaResponse fromEntity(Venditore v) {
        return new AziendaResponse(
                v.getId(),
                v.getRagioneSociale(),
                v.getCoordinate(),
                v.getRuolo().toString()
        );
    }
}
