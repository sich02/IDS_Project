package org.example.controller;

import org.example.model.Prodotto;
import org.example.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/curatore")
public class CuratoreController {

    @Autowired
    private ProdottoRepository prodottoRepo;

    /**
     * Endpoint 1: DASHBOARD REVISIONI
     * Restituisce al Frontend la lista di tutti i prodotti in attesa di validazione.
     * URL: GET /api/curatore/revisioni
     */
    @GetMapping("/revisioni")
    public ResponseEntity<List<Prodotto>> visualizzaCodaRevisioni() {
        // Usa il metodo corretto del Repository (cerca per stringa)
        List<Prodotto> inRevisione = prodottoRepo.findByStatoNome("IN_APPROVAZIONE");
        return ResponseEntity.ok(inRevisione);
    }

    /**
     * Endpoint 2: APPROVAZIONE
     * Il Curatore accetta il prodotto, che passa allo stato PUBBLICATO.
     * URL: PUT /api/curatore/approva/{id}
     */
    @PutMapping("/approva/{id}")
    public ResponseEntity<String> approvaProdotto(@PathVariable Long id) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con ID: " + id));

        try {
            // 1. Logica di Business (Pattern State)
            prodotto.pubblica();

            // 2. Persistenza (Salva il nuovo statoNome "PUBBLICATO" nel DB)
            prodottoRepo.save(prodotto);

            return ResponseEntity.ok("Prodotto approvato e pubblicato con successo.");

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }

    /**
     * Endpoint 3: RIFIUTO
     * Il Curatore rimanda il prodotto in BOZZA specificando un motivo.
     * URL: PUT /api/curatore/rifiuta/{id}
     * Body: "motivo": "Foto non conforme..."
     */
    @PutMapping("/rifiuta/{id}")
    public ResponseEntity<String> rifiutaProdotto(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con ID: " + id));

        String motivazione = body.get("motivo");

        try {
            // 1. Logica di Business (Pattern State)
            prodotto.rifiuta(motivazione);

            // 2. Persistenza (Salva il nuovo statoNome "BOZZA")
            prodottoRepo.save(prodotto);

            return ResponseEntity.ok("Prodotto rifiutato. Tornato in stato BOZZA.");

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}