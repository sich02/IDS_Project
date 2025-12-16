package org.example.service;

import  org.example.factory.UtenteFactory;
import  org.example.model.RuoloUtente;
import  org.example.model.Utente;
import  org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private UtenteFactory utenteFactory;

    //REGSTRAZIONE
    public Utente registraUtente(Map<String, Object>dati){
        String email = (String)dati.get("email");
        if (utenteRepo.existsByEmail(email)){
            throw new IllegalArgumentException("Account già esistente");
        }
        RuoloUtente ruolo = RuoloUtente.valueOf((String) dati.get("ruolo"));
        Utente nuovoUtente = utenteFactory.creaUtente(ruolo, dati);

        return utenteRepo.save(nuovoUtente);
    }

    //LOGIN
    public Utente login(String email, String password){
        Utente u = utenteRepo.findByEmail(email).orElse(null);
        if (u != null && u.getPassword().equals(password)){
            return u;
        }
        return null;
    }
}
