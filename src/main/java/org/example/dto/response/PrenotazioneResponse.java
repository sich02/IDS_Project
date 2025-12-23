package org.example.dto.response;

import org.example.model.Prenotazione;

public record PrenotazioneResponse(
        Long idPrenotazione,
        String nomeEvento,
        String dataEvento,
        String luogo,
        int postiPrenotati,
        double prezzoTotale
) {
    public static PrenotazioneResponse fromEntity(Prenotazione p) {
        return new PrenotazioneResponse(
                p.getId(),
                p.getEvento().getNome(),
                p.getEvento().getDataEvento().toString(),
                p.getEvento().getLuogo(),
                p.getNumeroPosti(),
                p.getEvento().getPrezzoBiglietto() * p.getNumeroPosti()
        );
    }
}
