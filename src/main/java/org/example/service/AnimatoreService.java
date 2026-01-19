package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.request.CreaEventoRequest;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.dto.request.ModificaEventoRequest;
import org.example.model.state.StatoBozza;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnimatoreService {
    @Autowired
    InvitoRepository invitoRepo;
    @Autowired
    EventoRepository eventoRepo;
    @Autowired
    UtenteRepository utenteRepo;
    @Autowired private
    PrenotazioneRepository prenotazioneRepo;

    //crea evento
    @Transactional
    public Evento creaEvento(CreaEventoRequest request) {
        Animatore animatore = (Animatore) utenteRepo.findById(request.idAnimatore())
                .orElseThrow(()-> new RuntimeException("Animatore non trovato"));

        LocalDateTime dataEvento =  LocalDateTime.parse(request.dataEvento(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Evento evento = new Evento(request.nome(), request.descrizione(), dataEvento,
                                   request.luogo(), request.maxPartecipanti(), request.prezzoBiglietto(), animatore);
        return eventoRepo.save(evento);
    }

    //invio in revisione al curatore
    @Transactional
    public void richiediPubblicazione(Long idEvento){
        Evento evento = eventoRepo.findById(idEvento)
                .orElseThrow(()-> new RuntimeException("Evento non trovato"));
        evento.richiediApprovazione();
        eventoRepo.save(evento);
    }

    //invita un venditore
    @Transactional
    public void invitaVenditore(Long idEvento, Long idVenditore){
        Evento evento = eventoRepo.findById(idEvento)
                .orElseThrow(()-> new RuntimeException("Evento non trovato"));

        Utente utente = utenteRepo.findById(idVenditore)
                .orElseThrow(()-> new RuntimeException("Venditore non trovato"));
        
        if(!(utente instanceof Venditore)){
            throw new  RuntimeException("L'utente selezionato non è un venditore");
        }

        Invito invito = new Invito(evento, (Venditore) utente);
        invitoRepo.save(invito);
    }

    //lista degli inviti
    public List<Invito> getInvitiEvento(Long idEvento){
        Evento evento = eventoRepo.findById(idEvento)
                .orElseThrow(()-> new RuntimeException("Evento non trovato"));
        return invitoRepo.findByEvento(evento);
    }

    //visualizza la lista degli acquirenti che partecipano all'evento
    public List<Acquirente> getPartecipanti(Long idEvento){
        Evento evento = eventoRepo.findById(idEvento).orElseThrow(()
                -> new RuntimeException("Evento non trovato"));

        return prenotazioneRepo.findByEvento(evento).stream()
                .map(Prenotazione :: getAcquirente)
                .toList();
    }

    //modifica evento
    @Transactional
    public Evento modificaEvento(ModificaEventoRequest request){
        Evento evento = eventoRepo.findById(request.idEvento())
                .orElseThrow(()-> new RuntimeException("Evento non trovato"));

        if (!evento.getOrganizzatore().getId().equals(request.idAnimatore())){
            throw new RuntimeException("Non puoi modificare un evento non tuo");
        }

        evento.setNome(request.nome());
        evento.setDescrizione(request.descrizione());
        evento.setLuogo(request.luogo());
        evento.setPrezzoBiglietto(request.prezzoBiglietto());
        evento.setPrezzoBiglietto(request.prezzoBiglietto());

        if (request.dataEvento()!=null){
            evento.setDataEvento(LocalDateTime.parse(request.dataEvento(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        evento.setStato(new StatoBozza());

        return eventoRepo.save(evento);
    }

    //elimina evento
    @Transactional
    public void eliminaEvento(Long idEvento, Long idAnimatore){
        Evento evento = eventoRepo.findById(idEvento)
                .orElseThrow(()-> new RuntimeException("Evento non trovato"));

        if (!evento.getOrganizzatore().getId().equals(idAnimatore)){
            throw new RuntimeException("Non puoi eliminare un evento non tuo");
        }

        eventoRepo.delete(evento);
    }

}
