package org.example.controller;


import org.example.dto.response.ProdottoResponse;
import org.example.dto.request.CreaProcessoRequest;
import org.example.service.TrasformazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trasformatore")
public class TrasformatoreController {
    @Autowired
    private TrasformazioneService trasformazioneService;

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
}
