package org.example.controller;

import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegistrazioneRequest;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class  AuthController {

    @Autowired
    private AuthService authService;


    //REGISTRAZIONE
    @PostMapping("/registrazione")
    public ResponseEntity<String> registrazione(@RequestBody RegistrazioneRequest request) {
        try{
            authService.registraUtente(request);
            return ResponseEntity.ok("Registrazione avvenuta con successo");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Errore registrazione: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore generico: " + e.getMessage());
        }
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var utente = authService.login(request);
        if (utente != null) {
            return ResponseEntity.ok(utente);
        }
        return ResponseEntity.status(401).body("Credenziale non valide");
    }
}