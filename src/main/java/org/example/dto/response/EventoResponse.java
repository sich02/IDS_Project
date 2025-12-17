package org.example.dto.response;

import org.example.model.Evento;
public record EventoResponse(
        Long id,
        String descrizione,
        String dataEvento,
        String luogo,
        int postiDisponibili,
        double prezzo,
        String organizzatore,
        String stato
) {
    public static EventoResponse fromEntity(Evento e) {
        return new EventoResponse(
                e.getId(),
                e.getDescrizione(),
                e.getDataEvento().toString(),
                e.getLuogo(),
                e.getMaxPartecipanti(),
                e.getPrezzoBiglietto(),
                e.getOrganizzatore().getNome()+" "+ e.getOrganizzatore().getCognome(),
                e.getStatoNome()
        );
    }
}
