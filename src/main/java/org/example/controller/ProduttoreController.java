package org.example.controller;

import org.example.model.*;
import org.example.repository.ProdottoRepository;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produttore")
public class ProduttoreController {

    @Autowired
    private ProdottoRepository prodottoRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    // --- 1. CREAZIONE PRODOTTO (Crea un ProdottoSingolo) ---
    @PostMapping("/crea-prodotto")
    public ResponseEntity<Prodotto> creaProdotto(@RequestBody Map<String, Object> dati) {

        // 1. Identifica il Produttore (che è un Venditore)
        Long idProduttore = Long.parseLong(dati.get("idProduttore").toString());
        Venditore produttore = (Venditore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        // 2. Estrai i dati dal JSON
        String nome = (String) dati.get("nome");
        String descrizione = (String) dati.get("descrizione");
        double prezzo = Double.parseDouble(dati.get("prezzo").toString());

        // 3. Istanzia la classe concreta ProdottoSingolo
        // (Nota: lo stato iniziale sarà BOZZA automaticamente grazie al costruttore di Contenuto)
        ProdottoSingolo nuovoProdotto = new ProdottoSingolo(nome, descrizione, prezzo, produttore);

        // 4. Salva (Spring salverà nella tabella 'prodotto_singolo' e 'prodotto' e 'contenuto')
        prodottoRepo.save(nuovoProdotto);

        return ResponseEntity.ok(nuovoProdotto);
    }

    // --- 2. VISUALIZZA I MIEI PRODOTTI ---
    @GetMapping("/i-miei-prodotti/{idProduttore}")
    public ResponseEntity<List<Prodotto>> getMieiProdotti(@PathVariable Long idProduttore) {

        Venditore produttore = (Venditore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        // Usa il metodo del repository che filtra per Venditore
        return ResponseEntity.ok(prodottoRepo.findByVenditore(produttore));
    }

    // --- 3. INVIA IN REVISIONE (Usa logica di Contenuto) ---
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {

        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Chiama il metodo ereditato da Contenuto -> Stato
        prodotto.richiediApprovazione();

        prodottoRepo.save(prodotto); // Salva il nuovo statoNome

        return ResponseEntity.ok("Prodotto inviato al Curatore. Stato attuale: " + prodotto.getStatoNome());
    }
}