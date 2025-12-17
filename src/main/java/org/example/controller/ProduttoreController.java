package org.example.controller;

import org.example.dto.request.CreaProdottoRequest;
import org.example.service.ProduttoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produttore")
public class ProduttoreController {

    @Autowired
    private ProduttoreService produttoreService;

    //creo il prodotto

    @PostMapping("/crea-prodotto")
    public ResponseEntity<?> creaProdotto(@RequestBody CreaProdottoRequest request) {
        try {
            var prodotto = produttoreService.creaProdotto(request);
            return ResponseEntity.ok(prodotto);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore creazione: " + e.getMessage());
        }
    }

    //visualizzazione catalogo
    @GetMapping("/i-miei-prodotti/{idProduttore}")
    public ResponseEntity<?> getMieiProdotti(@PathVariable Long id) {
        return ResponseEntity.ok(produttoreService.getIMieiProdotti(id));
    }

    //mando in approvazione
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {
        produttoreService.richiediPubblicazione(idProdotto);
        return ResponseEntity.ok("Prodotto inviato al Curatore per la revisione");
    }

}