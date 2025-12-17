package org.example.dto.response;

import org.example.model.Invito;

public record InvitoResponse(
        Long id,
        String nomeEvento,
        String dataEvento,
        String nomeVenditore,
        String emailVenditore,
        boolean accettato
) {
    public static InvitoResponse fromEntity(Invito i) {
        return new InvitoResponse(
                i.getId(),
                i.getEvento().getNome(),
                i.getEvento().getDataEvento().toString(),
                i.getVenditore().getNome()+" "+i.getVenditore().getCognome(),
                i.getVenditore().getEmail(),
                i.isAccettato()
        );
    }
}
