package org.example.controller;

import org.example.model.Prodotto;
import org.example.service.CuratoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/curatore")
public class CuratoreController {

    @Autowired
    private CuratoreService curatoreService;

    //lista dei prodotti in revisione
    @GetMapping("/revisioni")
    public ResponseEntity<List<Prodotto>> visualizzaCodaRevisioni() {
        return ResponseEntity.ok(curatoreService.getProdottiInRevisione());
    }

    //approvazione
    @PutMapping("/approva/{id}")
    public ResponseEntity<String> approvaProdotto(@PathVariable Long id) {
        try{
            curatoreService.approvaProdotto(id);
            return ResponseEntity.ok("Prodotto approvato e pubblicato");
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Errore: "  + e.getMessage());
        }
    }

    //rifiuto con motivazione
    @PutMapping("/rifiuta/{id}")
    public ResponseEntity<String> rifiutaProdotto(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try{
            curatoreService.rifiutaProdotto(id, body.get("motivo"));
            return ResponseEntity.ok("Prodotto rifiutato (Bozza)");
        }catch(Exception e){
            return  ResponseEntity.badRequest().body("Errore: "  + e.getMessage());
        }
    }
}