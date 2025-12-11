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

    //creo il prodotto

    @PostMapping("/crea-prodotto")
    public ResponseEntity<?> creaProdotto(@RequestBody Map<String, Object> dati) {
        try {

            Long idProduttore = Long.valueOf(dati.get("idProduttore").toString());

            Venditore produttore = (Venditore) utenteRepo.findById(idProduttore)
                    .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

            String nome = (String) dati.get("nome");
            String descrizione = (String) dati.get("descrizione");
            double prezzo = Double.valueOf(dati.get("prezzo").toString());

            ProdottoSingolo nuovoProdotto = new ProdottoSingolo(nome, descrizione, prezzo, produttore);

            if(dati.containsKey("certificazioni")){
                List<Map<String, String>> certsInput = (List<Map<String, String>>) dati.get("certificazioni");

                for(Map<String, String> cMap: certsInput){
                    TipoCertificazione tipo =  TipoCertificazione.valueOf(cMap.get("nome"));

                    Certificazione cert = new Certificazione(
                            tipo,
                            cMap.get("enteRilascio"),
                            cMap.get("descrizione")
                    );
                    nuovoProdotto.aggiungiCertificazione(cert);
                }
            }

            prodottoRepo.save(nuovoProdotto);

            return ResponseEntity.ok(nuovoProdotto);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Errore: Tipo certificazione non valido. Valori accettati: DOP, IGP, BIO, ecc.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore creazione: " + e.getMessage());
        }
    }

    //visualizzazione catalogo
    @GetMapping("/i-miei-prodotti/{idProduttore}")
    public ResponseEntity<List<Prodotto>> getMieiProdotti(@PathVariable Long idProduttore) {

        Venditore produttore = (Venditore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        return ResponseEntity.ok(prodottoRepo.findByVenditore(produttore));
    }

    //mando in approvazione
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {
        try {
            Prodotto prodotto = prodottoRepo.findById(idProdotto)
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

            prodotto.richiediApprovazione();

            prodottoRepo.save(prodotto);

            return ResponseEntity.ok("Prodotto inviato al Curatore. Stato attuale: " + prodotto.getStatoNome());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}