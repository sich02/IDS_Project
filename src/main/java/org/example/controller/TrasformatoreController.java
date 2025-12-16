package org.example.controller;

import org.example.model.Prodotto;
import org.example.service.TrasformazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trasformatore")
public class TrasformatoreController {
    @Autowired
    private TrasformazioneService trasformazioneService;

    @PostMapping("/crea-processo")
    public ResponseEntity<?> creaProcesso(@RequestBody Map<String, Object> dati) {
        try{
            Prodotto p = trasformazioneService.creaProcessoTrasformazione(dati);
            return ResponseEntity.ok("Trasformazione completata. Nuovo prodotto creato: " +p.getNome());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Errore: "+e.getMessage());
        }
    }

    //manda in approvazione
    @PutMapping("/pubblica/{idProdotto}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idProdotto) {
        try{
            trasformazioneService.richiediPubblicazione(idProdotto);
            return ResponseEntity.ok("Prodotto inviato al Curatore");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //lista prodotti trasformati
    @GetMapping("/i-miei-prodotti/{idTrasformatore}")
    public ResponseEntity<List<Prodotto>> getMieiProdotti(@PathVariable Long idTrasformatore) {
        try{
            return ResponseEntity.ok(trasformazioneService.getProdottiTrasformatore(idTrasformatore));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
