package org.example.controller;

import org.example.dto.request.CreaPacchettoRequest;
import org.example.dto.request.ModificaPacchettoRequest;
import org.example.dto.response.InvitoResponse;
import org.example.dto.response.OrdineResponse;
import org.example.repository.ProdottoRepository;
import org.example.service.DistributoreService;
import org.example.service.SocialService;
import org.example.service.VenditoreService;
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
    @Autowired
    private ProdottoRepository prodottoRepo;
    @Autowired
    private SocialService socialService;

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

    //modifica pacchetto già approvato
    @PutMapping("/modifica-pacchetto")
    public ResponseEntity<?> modificaPacchetto(@RequestBody ModificaPacchettoRequest request){
        try{
            var pacchetto = distributoreService.modificaPacchetto(request);
            return ResponseEntity.ok().body(pacchetto);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Errore modifica: " + e.getMessage());
        }
    }

    //elimina pacchetto
    @DeleteMapping("/elimina-pacchetto/{idPacchetto}")
    public ResponseEntity<String> eliminaPacchetto(@PathVariable Long idPacchetto, @RequestParam Long idDistributore){
        try{
            distributoreService.eliminaPacchetto(idPacchetto, idDistributore);
            return ResponseEntity.ok().body("elimina pacchetto");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Errore nell'eliminazione: " + e.getMessage());
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

    //condivisione
    @PostMapping("/condividi/{idPacchetto}")
    public ResponseEntity<String> condividiPacchetto(@PathVariable Long idPacchetto, @RequestParam Long idDistributore) {
        try {
            var pacchetto = prodottoRepo.findById(idPacchetto)
                    .orElseThrow(() -> new RuntimeException("Pacchetto non trovato"));

            // Verifica possesso
            if (!pacchetto.getVenditore().getId().equals(idDistributore)) {
                return ResponseEntity.status(403).body("Non puoi condividere un pacchetto non tuo.");
            }

            // Verifica stato
            if (!"PUBBLICATO".equals(pacchetto.getStatoNome())) {
                return ResponseEntity.badRequest().body("Il pacchetto deve essere pubblicato per essere condiviso.");
            }

            // Notifica gli Observer
            socialService.condividiContenuto(pacchetto);

            return ResponseEntity.ok("Pacchetto condiviso con successo sui social.");
        } catch (Exception e) {
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

    //------GESTIONE ORDINI------

    //visualizza ordini
    @GetMapping("/ordini/{idDistributore}")
    public ResponseEntity<List<OrdineResponse>> getOrdini(@PathVariable Long idDistributore) {
        try{
            var ordini = venditoreService.getOrdiniRicevuti(idDistributore);
            return ResponseEntity.ok(ordini.stream()
                    .map(OrdineResponse::fromEntity)
                    .toList());
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //spedisci ordine
    @PutMapping("/ordini/spedisci/{idOrdine}")
    public ResponseEntity<String> spedisciOrdine(@PathVariable Long idOrdine, @RequestParam Long idDistributore) {
        try{
            venditoreService.spedisciOrdine(idOrdine, idDistributore);
            return ResponseEntity.ok().body("Ordine spedito e magazzino aggiornato");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}
