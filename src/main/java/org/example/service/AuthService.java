package org.example.service;

import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegistrazioneRequest;
import org.example.factory.UtenteFactory;
import org.example.model.RuoloUtente;
import org.example.model.Utente;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private UtenteFactory utenteFactory;

    //registrazoine
    public Utente registraUtente(RegistrazioneRequest request) {
        if (utenteRepo.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Account già esistente");
        }

        Utente nuovoUtente = utenteFactory.creaUtente(request);
        return utenteRepo.save(nuovoUtente);
    }

    //login
    public Utente login(LoginRequest request) {
        Utente u = utenteRepo.findByEmail(request.email()).orElse(null);
        if (u != null && u.getPassword().equals(request.password())) {
            return u;
        }
        return null;
    }

    //logout
    public void logout (Long idUtente){
        if (idUtente != null && !utenteRepo.existsById(idUtente)) {
            throw new IllegalArgumentException("Utente non trovata");
        }
    }
}
