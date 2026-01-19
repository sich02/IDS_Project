package org.example.controller;

import org.example.dto.request.CreaEventoRequest;
import org.example.dto.response.EventoResponse;
import org.example.dto.response.InvitoResponse;
import org.example.service.AnimatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.dto.request.ModificaEventoRequest;
import org.example.dto.response.PartecipanteResponse;
import org.example.repository.EventoRepository;
import org.example.service.SocialService;

import java.util.List;

@RestController
@RequestMapping("/api/animatore")
public class AnimatoreController {
    @Autowired
    private AnimatoreService animatoreService;
    @Autowired
    private EventoRepository eventoRepo;
    @Autowired
    private SocialService socialService;

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

    //modifica l'evento
    @PutMapping
    public ResponseEntity<?> modificaEvento(@RequestBody ModificaEventoRequest request) {
        try{
            var evento = animatoreService.modificaEvento(request);
            return ResponseEntity.ok(EventoResponse.fromEntity(evento));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Errore nella modificaEvento: "+e.getMessage());
        }
    }

    //rimuovi evento
    @PutMapping
    public ResponseEntity<String> eliminaEvento(@PathVariable Long idEvento, @RequestParam Long idAnimatore) {
        try{
            animatoreService.eliminaEvento(idEvento, idAnimatore);
            return ResponseEntity.ok("Evento modificado com sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Errore nella eliminaEvento: "+e.getMessage());
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

    //visualizza la lista degli acquirenti che partecipano all'evento
    @GetMapping("/eventi/{idEvento}/partecipanti")
    public ResponseEntity<List<org.example.dto.response.PartecipanteResponse>> getPartecipanti(@PathVariable Long idEvento){
        try{
            var acquirenti = animatoreService.getPartecipanti(idEvento);
            var response = acquirenti.stream()
                    .map(org.example.dto.response.PartecipanteResponse::fromEntity)
                    .toList();
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //condivisione
    @PostMapping("/condividi/{idEvento}")
    public ResponseEntity<String> condividiEvento(@PathVariable Long idEvento, @RequestParam Long idAnimatore){
        try{
            var evento = eventoRepo.findById(idEvento)
                    .orElseThrow(() -> new RuntimeException("Evento non trovato"));

            if(!evento.getOrganizzatore().getId().equals(idAnimatore)){
                return ResponseEntity.status(403).body("non puoi condividere un evento che non hai organizzato");
            }

            String stato = evento.getStatoNome();
            if(!"PUBBLICATO".equals(evento.getStatoNome())){
                return ResponseEntity.badRequest().body("L'evento deve essere pubblicato per essre condiviso; Stato attuale: "+stato);
            }
            socialService.condividiContenuto(evento);
            return ResponseEntity.ok("Evento inviato in approvazione al curatore");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
