package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class AnimatoreService {
    @Autowired
    InvitoRepository invitoRepo;
    @Autowired
    EventoRepository eventoRepo;
    @Autowired
    UtenteRepository utenteRepo;

    //crea evento
    @Transactional
    public Evento creaEvento(Map<String, Object> dati){
        Long idAnimatore = Long.valueOf(dati.get("idAnimatore").toString());
        Animatore animatore = (Animatore) utenteRepo.findById(idAnimatore)
                .orElseThrow(()-> new RuntimeException("Animatore non trovato"));

        String nome = String.valueOf(dati.get("nome"));
        String descrizione = String.valueOf(dati.get("descrizione"));
        String luogo = String.valueOf(dati.get("luogo"));
        int maxPartecipanti = Integer.parseInt(dati.get("maxPartecipanti").toString());
        double prezzo = Double.parseDouble(dati.get("prezzo").toString());

        LocalDateTime dataEvento =  LocalDateTime.parse(String.valueOf(dati.get("dataEvento")),
                                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Evento evento = new Evento(nome, descrizione, dataEvento, luogo, maxPartecipanti, prezzo, animatore);
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
}
