package org.example.controller;

import org.example.model.Prodotto;
import org.example.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curatore")
public class CuratoreController {

    @Autowired // Iniezione automatica
    private ProdottoRepository prodottoRepo;

    @GetMapping("/revisioni")
    public ResponseEntity<List<Prodotto>> visualizzaCodaRevisioni() {
        // Cerca usando la stringa salvata nel DB
        return ResponseEntity.ok(prodottoRepo.findByStatoNome("IN_APPROVAZIONE"));
    }

    @PutMapping("/approva/{id}")
    public ResponseEntity<String> approvaProdotto(@PathVariable Long id) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        prodotto.pubblica(); // Cambia lo stato
        prodottoRepo.save(prodotto); // Salva (metodo standard JPA)

        return ResponseEntity.ok("Prodotto approvato e pubblicato.");
    }

    @PutMapping("/rifiuta/{id}")
    public ResponseEntity<String> rifiutaProdotto(@PathVariable Long id, @RequestBody String motivo) {
        Prodotto prodotto = prodottoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        prodotto.rifiuta(motivo); // Cambia lo stato
        prodottoRepo.save(prodotto); // Salva

        return ResponseEntity.ok("Prodotto rifiutato.");
    }
}