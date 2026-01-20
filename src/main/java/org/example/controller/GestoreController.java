package org.example.controller;

import org.example.dto.response.UtenteResponse;
import org.example.service.GestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spi/gestore")
public class GestoreController {
    @Autowired
    private GestoreService gestoreService;

    //visualizza coda richieste
    @GetMapping("/richieste")
    public ResponseEntity<List<UtenteResponse>> visualizzaRichieste(){
        var richieste = gestoreService.getRichiesteRegistrazione();
        return  ResponseEntity.ok(richieste.stream()
                .map(UtenteResponse :: fromEntity).toList());
    }

    //approva
    @PutMapping("/approva/{idUtente}")
    public ResponseEntity<String> approvaUtente(@PathVariable Long idUtente){
        try{
            gestoreService.approvaUtente(idUtente);
            return ResponseEntity.ok("Utente accreditato con successo");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //rifiuta
    @DeleteMapping("/rifiuta/{idUtente}")
    public ResponseEntity<String> eliminaUtente(@PathVariable Long idUtente, @RequestParam String motivazione){
        try{
            if (motivazione == null || motivazione.trim().isEmpty()){
                return ResponseEntity.badRequest().body("Motivazione mancante");
            }

            gestoreService.rifiutaUtente(idUtente, motivazione);
            return ResponseEntity.ok("Utente rifiutato con successo e utente rimosso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
