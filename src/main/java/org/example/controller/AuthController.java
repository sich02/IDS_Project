package org.example.controller;

import org.example.model.Utente;
import org.example.model.RuoloUtente;
import org.example.factory.UtenteFactory;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class  AuthController {

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private UtenteFactory utenteFactory;

    @PostMapping("/registrazione")
    public ResponseEntity<String> registrazione(@RequestBody Map<String, Object> dati) {

        //Controllo per vedere se esiste già un account con quella mail
        String email = (String) dati.get("email");
        if(utenteRepo.existsByEmail(email)){
            return ResponseEntity.status(400).body("account già esistente");
        }

        RuoloUtente ruolo = RuoloUtente.valueOf((String) dati.get("ruolo"));
        Utente nuovoUtente = utenteFactory.creaUtente(ruolo, dati);

        utenteRepo.save(nuovoUtente);

        return ResponseEntity.ok("Utente registrato con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<Utente> login(@RequestBody Map<String, String> credenziali) {
        String email = credenziali.get("email");
        String password = credenziali.get("password");


        Utente u = utenteRepo.findByEmail(email).orElse(null);

        if (u != null && u.getPassword().equals(password)) {
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.status(401).build();
    }
}