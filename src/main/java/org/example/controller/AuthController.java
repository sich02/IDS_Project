package org.example.controller;

import org.example.model.Utente;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class  AuthController {

    @Autowired
    private AuthService authService;


    //REGISTRAZIONE
    @PostMapping("/registrazione")
    public ResponseEntity<String> registrazione(@RequestBody Map<String, Object> dati) {

        try{
            authService.registraUtente(dati);
            return ResponseEntity.ok("Registrazione avvenuta con successo");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Errore registrazione: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore generico: " + e.getMessage());
        }
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenziali) {
        String email = credenziali.get("email");
        String password = credenziali.get("password");


        Utente utente = authService.login(email, password);

        if (utente != null) {
            return ResponseEntity.ok(utente);
        }
        return ResponseEntity.status(401).body("Credenziale non valide");
    }
}