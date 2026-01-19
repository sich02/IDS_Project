package org.example.controller;

import org.example.dto.request.CreaProdottoRequest;
import org.example.dto.request.ModificaProdottoSingoloRequest;
import org.example.dto.response.InvitoResponse;
import org.example.dto.response.ProdottoResponse;
import org.example.service.ProduttoreService;
import org.example.service.VenditoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.repository.ProdottoRepository;
import org.example.service.SocialService;

import java.util.List;

@RestController
@RequestMapping("/api/produttore")
public class ProduttoreController {

    @Autowired
    private ProduttoreService produttoreService;
    @Autowired
    private VenditoreService venditoreService;
    @Autowired
    private SocialService socialService;
    @Autowired
    private ProdottoRepository prodottoRepo;

    //------GESTIONE PRODOTTI------

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

    //modifica un prodotto già approvato
    @PutMapping("/modifica-prodotto")
    public ResponseEntity<?> modificaProdotto(@RequestBody ModificaProdottoSingoloRequest request) {
        try{
            var  prodotto = produttoreService.modificaProdotto(request);
            return ResponseEntity.ok(ProdottoResponse.fromEntity(prodotto));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore modifica: " + e.getMessage());
        }
    }

    //elimina prodotto
    @DeleteMapping("/elimina-prodotto/{idProdotto}")
    public ResponseEntity<String> eliminaProdotto(@PathVariable Long idProdotto, @RequestParam Long idProduttore) {
        try{
            produttoreService.eliminaProdotto(idProdotto, idProduttore);
            return ResponseEntity.ok("Prodotto eliminato con successo");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore nell'eliminazione: " + e.getMessage());
        }
    }

    //mando in approvazione
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {
        produttoreService.richiediPubblicazione(idProdotto);
        return ResponseEntity.ok("Prodotto inviato al Curatore per la revisione");
    }

    //condivisione
    @PostMapping("/condividi/{idProdotto}")
    public ResponseEntity<String> condividiProdotto(@PathVariable Long idProdotto, @RequestParam Long idProduttore) {
        try {
            var prodotto = prodottoRepo.findById(idProdotto)
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

            if (!prodotto.getVenditore().getId().equals(idProduttore)) {
                return ResponseEntity.status(403).body("Non puoi condividere un prodotto non tuo.");
            }

            if (!"PUBBLICATO".equals(prodotto.getStatoNome())) {
                return ResponseEntity.badRequest().body("Il prodotto deve essere pubblicato per essere condiviso.");
            }

            socialService.condividiContenuto(prodotto);

            return ResponseEntity.ok("Prodotto condiviso su tutti i canali social configurati.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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