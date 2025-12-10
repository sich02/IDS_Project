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

    /**
     * 1. CREAZIONE PRODOTTO
     * Il produttore crea un nuovo oggetto. Lo stato iniziale sarà "BOZZA" (automatico).
     */
    @PostMapping("/crea-prodotto")
    public ResponseEntity<?> creaProdotto(@RequestBody Map<String, Object> dati) {
        try {
            // 1. Recupero il Produttore (convertendo l'ID da Integer/Long in sicurezza)
            Long idProduttore = Long.valueOf(dati.get("idProduttore").toString());

            Venditore produttore = (Venditore) utenteRepo.findById(idProduttore)
                    .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

            // 2. Estrazione dati
            String nome = (String) dati.get("nome");
            String descrizione = (String) dati.get("descrizione");
            double prezzo = Double.valueOf(dati.get("prezzo").toString());

            // 3. Creazione (Stato -> BOZZA)
            ProdottoSingolo nuovoProdotto = new ProdottoSingolo(nome, descrizione, prezzo, produttore);

            // 4. Salvataggio
            prodottoRepo.save(nuovoProdotto);

            return ResponseEntity.ok(nuovoProdotto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore creazione: " + e.getMessage());
        }
    }

    /**
     * 2. VISUALIZZA CATALOGO PERSONALE
     */
    @GetMapping("/i-miei-prodotti/{idProduttore}")
    public ResponseEntity<List<Prodotto>> getMieiProdotti(@PathVariable Long idProduttore) {
        // Cerco il venditore per sicurezza
        Venditore produttore = (Venditore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        // Uso il metodo del repo: findByVenditore
        return ResponseEntity.ok(prodottoRepo.findByVenditore(produttore));
    }

    /**
     * 3. PUBBLICAZIONE (Bozza -> In Approvazione)
     * Il produttore decide che il prodotto è pronto e chiede al Curatore di controllarlo.
     */
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {
        try {
            Prodotto prodotto = prodottoRepo.findById(idProdotto)
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

            // Cambio stato: BOZZA -> IN_APPROVAZIONE
            prodotto.richiediApprovazione();

            // Salvo il cambiamento
            prodottoRepo.save(prodotto);

            return ResponseEntity.ok("Prodotto inviato al Curatore. Stato attuale: " + prodotto.getStatoNome());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}