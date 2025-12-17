package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenditoreService {
    @Autowired
    private InvitoRepository invitoRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    public List<Invito> getMieiInviti(Long idVenditore) {
        Utente utente = utenteRepo.findById(idVenditore)
                .orElseThrow(()-> new RuntimeException("Utente non trovata"));

        if(!(utente instanceof Venditore)){
            throw new RuntimeException("L'utente specifico non è un venditore");
        }

        return invitoRepo.findByVenditore((Venditore) utente);
    }

    @Transactional
    public void gestisciInvito(Long idInvito, boolean accetta) {
        Invito invito = invitoRepo.findById(idInvito)
                .orElseThrow(()-> new EntityNotFoundException("Invito non trovata"));

        invito.setAccettato(accetta);
        invitoRepo.save(invito);

    }
}
