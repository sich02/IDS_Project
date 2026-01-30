package org.example.controller;

import org.example.dto.response.ProdottoResponse;
import org.example.model.MetodoTrasformazione;
import org.example.service.CuratoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/curatore")
public class CuratoreController {

    @Autowired
    private CuratoreService curatoreService;

    //-----CONTENUTI-----

    //lista dei prodotti in revisione
    @GetMapping("/revisioni")
    public ResponseEntity<List<ProdottoResponse>> visualizzaCodaRevisioni() {
        var prodotti = curatoreService.getProdottiInRevisione();
        return ResponseEntity.ok(prodotti.stream().map(ProdottoResponse::fromEntity).toList());
    }

    //approvazione
    @PutMapping("/approva/{id}")
    public ResponseEntity<String> approvaProdotto(@PathVariable Long id) {
        try{
            curatoreService.approvaProdotto(id);
            return ResponseEntity.ok("Prodotto approvato e pubblicato");
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Errore: "  + e.getMessage());
        }
    }

    //rifiuto con motivazione
    @PutMapping("/rifiuta/{id}")
    public ResponseEntity<String> rifiutaProdotto(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try{
            curatoreService.rifiutaProdotto(id, body.get("motivo"));
            return ResponseEntity.ok("Prodotto rifiutato (Bozza)");
        }catch(Exception e){
            return  ResponseEntity.badRequest().body("Errore: "  + e.getMessage());
        }
    }

    //-----METODI-----

    //lista metodi in revisione
    @GetMapping("/revisioni-metodi")
    public ResponseEntity<List<MetodoTrasformazione>> visualizzaRevisioniMetodi() {
        return ResponseEntity.ok(curatoreService.getMetodiInRevisione());
    }

    //approva metodo
    @PutMapping("/approva-metodo/{id}")
    public ResponseEntity<String> approvaMetodo(@PathVariable Long id) {
        try{
            curatoreService.approvaMetodo(id);
            return ResponseEntity.ok("Metodo approvato");
        }catch(Exception e){
            return  ResponseEntity.badRequest().body("Errore: "  + e.getMessage());
        }
    }

    //rifiuta metodo
    @PutMapping("/rifiuta-metodo/{id}")
    public ResponseEntity<String> put(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try{
            curatoreService.rifiutaMetodo(id, body.get("motivo"));
            return ResponseEntity.ok("Metodo rifiutato");
        }catch(Exception e){
            return  ResponseEntity.badRequest().body("Errore: "  + e.getMessage());
        }
    }
}