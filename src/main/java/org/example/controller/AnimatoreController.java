package org.example.controller;

import org.example.dto.request.CreaEventoRequest;
import org.example.dto.response.EventoResponse;
import org.example.dto.response.InvitoResponse;
import org.example.service.AnimatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animatore")
public class AnimatoreController {
    @Autowired
    private AnimatoreService animatoreService;

    //creo l'evento
    @PostMapping("/crea-evento")
    public ResponseEntity<?> creaEvento(@RequestBody CreaEventoRequest request) {
        try{
            var evento = animatoreService.creaEvento(request);
            return ResponseEntity.ok(EventoResponse.fromEntity(evento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore nella crazione dell'evento: "+e.getMessage());
        }
    }

    //manda in approvazione
    @PutMapping("/pubblica/{idEvento}")
    public ResponseEntity<String> richiediPubblicazione(@PathVariable Long idEvento, @RequestParam Long idVenditore){
        try{
            animatoreService.richiediPubblicazione(idEvento);
            return ResponseEntity.ok("Evento inviato in approvazione al curatore");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //invita venditore
    @PostMapping("/invita")
    public ResponseEntity<String> invita(@RequestParam Long idEvento, @RequestParam Long idVenditore){
        try{
            animatoreService.invitaVenditore(idEvento,idVenditore);
            return ResponseEntity.ok("Invito mandato al venditore scelto");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //vedi lista inviti
    @GetMapping("/inviti/{idEvento}")
    public ResponseEntity<?> getInviti(@PathVariable Long idEvento) {
        try {
            var listaInviti = animatoreService.getInvitiEvento(idEvento);

            List<InvitoResponse> response = listaInviti.stream().map(InvitoResponse :: fromEntity).toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
