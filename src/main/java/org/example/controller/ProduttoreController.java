package org.example.controller;

import org.example.dto.request.CreaProdottoRequest;
import org.example.dto.response.InvitoResponse;
import org.example.service.ProduttoreService;
import org.example.service.VenditoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produttore")
public class ProduttoreController {

    @Autowired
    private ProduttoreService produttoreService;
    @Autowired
    private VenditoreService venditoreService;

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

    //------GESTIONE INVITI RICEVUTI------

    //visualizzazione degli inviti ricevuti
    @GetMapping("/i-miei-inviti/{idProduttore}")
    public ResponseEntity<List<InvitoResponse>> getMieiInviti(@PathVariable Long idProduttore) {
        try{
            var inviti = venditoreService.getMieiInviti(idProduttore);
            return ResponseEntity.ok(inviti.stream().map(InvitoResponse::fromEntity).toList());
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //accetta o rifiuta un invito a un evento
    @PutMapping("/inviti/gestisci/{idInvito}")
    public ResponseEntity<String> gestisciInviti(@PathVariable Long idInvito, @RequestParam boolean accetta) {
        try{
            venditoreService.gestisciInvito(idInvito, accetta);
            return ResponseEntity.ok(accetta ? "Invito accettato" : "Invito rifiutato");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}