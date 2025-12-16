package org.example.controller;

import org.example.model.Carrello;
import org.example.model.Prodotto;
import org.example.model.Ordine;
import org.example.model.Evento;
import org.example.service.AcquirenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acquirente")
public class AcquirenteController {

    @Autowired
    private AcquirenteService acquirenteService;

    //visualizza il catalogo
    @GetMapping("/catalogo")
    public ResponseEntity<List<Prodotto>> visualizzaCatalogo() {
        return ResponseEntity.ok(acquirenteService.getCatalogoPubblico());
    }

    //visualizza la lista degli eventi
    @GetMapping("/eventi")
    public ResponseEntity<List<Evento>> visualizzaEventi() {
        return ResponseEntity.ok(acquirenteService.getEventiDisponibili());
    }

    //visualizza il carrello
    @GetMapping("/carrello/{idAcquirente}")
    public ResponseEntity<?> visualizzaCarrello(@PathVariable Long idAcquirente) {
        try {
            Carrello carrello = acquirenteService.getCarrello(idAcquirente);
            return ResponseEntity.ok(carrello);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //aggiungi al carrello
    @PostMapping("/carrello/aggiungi")
    public ResponseEntity<String> aggiungiAlCarrello(@RequestParam Long idAcquirente, @RequestParam Long idProdotto) {
        try{
            acquirenteService.aggiungiAlCarrello(idAcquirente,idProdotto);
            return ResponseEntity.ok("Prodotto aggiunto al carrello");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //rimuovi dal carrello
    @DeleteMapping("/carrello/rimuovi")
    public ResponseEntity<String> rimuoviDalCarrello(@RequestParam Long idAcquirente, @RequestParam Long idProdotto) {
        try{
            acquirenteService.rimuoviDalCarrello(idAcquirente, idProdotto);
            return ResponseEntity.ok("Prodotto rimosso dal carrello");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //effettua ordine
    @PostMapping("/ordine/effettua")
    public ResponseEntity<String> effettuaOrdine(@RequestParam Long idAcquirente) {
        try{
            Ordine ordine = acquirenteService.effettuaOrdine(idAcquirente);
            return ResponseEntity.ok("Ordine effettuato! ID: " + ordine.getId() + " - Totale: " + ordine.getTotale() + "€");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //annulla ordine
    @PutMapping("/ordine/annulla/{idOrdine}")
    public ResponseEntity<String> annullaOrdine(@PathVariable Long idOrdine, @RequestParam Long idAcquirente) {
       try{
           acquirenteService.annullaOrdine(idOrdine, idAcquirente);
           return ResponseEntity.ok("Ordine annullato");
       }catch (Exception e){
           return  ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    //prenota evento
    @PostMapping("/evento/prenota")
    public ResponseEntity<String> prenotaEvento(@RequestParam Long idAcquirente, @RequestParam Long idEvento) {
        try{
            acquirenteService.prenotaEvento(idAcquirente, idEvento);
            return ResponseEntity.ok("Prenotazione confermata");
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //annulla la prenotazione all'evento
    @DeleteMapping
    public ResponseEntity<String> annullaPrenotazione(@PathVariable Long idPrenotazione, @RequestParam Long idAcquirente) {
        try{
            acquirenteService.annullaPrenotazione(idPrenotazione, idAcquirente);
            return ResponseEntity.ok("Prenotazione annullata");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}