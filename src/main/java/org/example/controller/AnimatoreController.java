package org.example.controller;

import org.example.model.Evento;
import org.example.model.Invito;
import org.example.service.AnimatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animatore")
public class AnimatoreController {
    @Autowired
    private AnimatoreService animatoreService;

    //creo l'evento
    @PostMapping("/crea-evento")
    public ResponseEntity<?> creaEvento(@RequestBody Map<String,Object> dati){
        try{
            Evento evento = animatoreService.creaEvento(dati);
            return ResponseEntity.ok(evento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore nella crazione dell'evento"+e.getMessage());
        }
    }

    //pubblicazione(manda alla lista del curatore)
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
    @GetMapping("/lista-inviti/{idVenditore}")
    public ResponseEntity<?> getInviti(@PathVariable Long idEvento){
        try{
            List<Invito> inviti = animatoreService.getInvitiEvento(idEvento);
            return ResponseEntity.ok(inviti);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
