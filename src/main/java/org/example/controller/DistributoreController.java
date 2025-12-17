package org.example.controller;

import org.example.dto.request.CreaPacchettoRequest;
import org.example.dto.response.InvitoResponse;
import org.example.dto.response.ProdottoResponse;
import org.example.service.VenditoreService;
import org.example.service.DistributoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distributore")
public class DistributoreController {
    @Autowired
    private DistributoreService distributoreService;
    @Autowired
    private VenditoreService venditoreService;

    //crea un pacchetto
    @PostMapping("/crea-pacchetto")
    public ResponseEntity<?> creaPacchetto(@RequestBody CreaPacchettoRequest request){
        try{
            var pacchetto = distributoreService.creaPacchetto(request);
            return ResponseEntity.ok().body(pacchetto);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }

    //visualizza la lista dei propri pacchetti
    @PostMapping("/i-miei-pacchetti/{id}")
    public ResponseEntity<String> iMieiPacchetto(@PathVariable Long id){
        try{
            var lista = distributoreService.getPacchettiDistributore(id);
            return ResponseEntity.ok().body(lista.toString());
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    //invia in approvazione un pacchetto
    @PostMapping("/pubblica/{id}")
    public ResponseEntity<String> pubblica(@PathVariable Long id){
        try{
            distributoreService.richiediPubblicazione(id);
            return ResponseEntity.ok().body("Pacchetto inviato in approvazione al curatore");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //------GESTIONE INVITI RICEVUTI------

    //visualizzazione degli inviti ricevuti
    @GetMapping("/i-miei-inviti/{idDistributore}")
    public ResponseEntity<List<InvitoResponse>> getMieiInviti(@PathVariable Long idDistributore) {
        try{
            var inviti = venditoreService.getMieiInviti(idDistributore);
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
