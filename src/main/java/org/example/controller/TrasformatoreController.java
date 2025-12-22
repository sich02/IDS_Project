package org.example.controller;


import org.example.dto.request.ModificaProdottoSingoloRequest;
import org.example.dto.response.InvitoResponse;
import org.example.dto.response.ProdottoResponse;
import org.example.dto.request.CreaProcessoRequest;
import org.example.service.TrasformazioneService;
import org.example.service.VenditoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trasformatore")
public class TrasformatoreController {
    @Autowired
    private TrasformazioneService trasformazioneService;
    @Autowired
    private VenditoreService venditoreService;


    //crea un prcesso di produzione
    @PostMapping("/crea-processo")
    public ResponseEntity<?> creaProcesso(@RequestBody CreaProcessoRequest request) {
        try{
            var prodotto = trasformazioneService.creaProcessoTrasformazione(request);
            return ResponseEntity.ok("Trasformazione completata. Nuovo prodotto creato: " +prodotto.getNome());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Errore: "+e.getMessage());
        }
    }

    //manda in approvazione
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {
        try{
            trasformazioneService.richiediPubblicazione(idProdotto);
            return ResponseEntity.ok("Prodotto inviato al Curatore per la revisione");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //modifica prodotto già approvato
    @PutMapping("/modifica-prodotto")
    public ResponseEntity<?> modificaProdotto(@RequestBody ModificaProdottoSingoloRequest request) {
       try {
           var prodotto = trasformazioneService.modificaProdotto(request);
           return ResponseEntity.ok("Prodotto modificato per la revisione");
       }catch (Exception e){
           return ResponseEntity.badRequest().body("Errore modifica: "+e.getMessage());
       }

    }

    //lista prodotti trasformati
    @GetMapping("/i-miei-prodotti/{idTrasformatore}")
    public ResponseEntity<List<ProdottoResponse>> getMieiProdotti(@PathVariable Long idTrasformatore) {
        try {
            var lista = trasformazioneService.getProdottiTrasformatore(idTrasformatore);
            return ResponseEntity.ok(lista.stream().map(ProdottoResponse :: fromEntity).toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //------GESTIONE INVITI RICEVUTI------

    //visualizzazione degli inviti ricevuti
    @GetMapping("/i-miei-inviti/{idTrasformatore}")
    public ResponseEntity<List<InvitoResponse>> getMieiInviti(@PathVariable Long idTrasformatore) {
        try{
            var inviti = venditoreService.getMieiInviti(idTrasformatore);
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
