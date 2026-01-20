package org.example.service;

import org.example.model.Utente;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class GestoreService {

    @Autowired
    private UtenteRepository utenteRepo;

    public List<Utente> getRichiesteRegistrazione(){
        return utenteRepo.findAll().stream().filter(u ->!u.isAccreditato()).toList();
    }

    //approva registrazione
    @Transactional
    public void approvaUtente(Long idUtente){
        Utente utente = utenteRepo.findById(idUtente)
                .orElseThrow(()->new RuntimeException("Utente non trovato"));

        if(utente.isAccreditato()){
            throw new RuntimeException("L'utente è già stato accreditato");
        }

        utente.setAccreditato(true);
        utenteRepo.save(utente);
    }

    //rifiuta registrazione
    @Transactional
    public void rifiutaUtente(Long idUtente, String motivazione){
        Utente utente = utenteRepo.findById(idUtente)
                .orElseThrow(()->new RuntimeException("Utente non trovato"));

        if(utente.isAccreditato()){
            throw new RuntimeException("Impossibile rifiutare un utente già attivo");
        }

        utenteRepo.delete(utente);
    }

}
