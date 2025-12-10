package org.example.controller;

import org.example.model.Prodotto;
import org.example.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired; // Mancava
import org.springframework.http.ResponseEntity; // Mancava
import org.springframework.web.bind.annotation.*; // Mancavano le annotazioni REST

import java.util.List;

@RestController // Mancava
@RequestMapping("/api/curatore") // Mancava
public class CuratoreController {

    @Autowired
    private ProdottoRepository prodottoRepo;

    // Rimosso il costruttore manuale, usiamo @Autowired

    @GetMapping("/revisioni")
    public ResponseEntity<List<Prodotto>> visualizzaCodaRevisioni() {
        // CORREZIONE: Usa il metodo standard findByStatoNome con la stringa "IN_APPROVAZIONE"
        return ResponseEntity.ok(prodottoRepo.findByStatoNome("IN_APPROVAZIONE"));
    }

    @PutMapping("/approva/{id}") // Trasformato in Endpoint REST
    public ResponseEntity<String> approvaProdotto(@PathVariable Long id) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        prodotto.pubblica();
        prodottoRepo.save(prodotto); // CORREZIONE: usa save()

        return ResponseEntity.ok("Prodotto approvato.");
    }

    @PutMapping("/rifiuta/{id}")
    public ResponseEntity<String> rifiutaProdotto(@PathVariable Long id, @RequestBody String motivo) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        prodotto.rifiuta(motivo);
        prodottoRepo.save(prodotto); // CORREZIONE: usa save()

        return ResponseEntity.ok("Prodotto rifiutato.");
    }
}