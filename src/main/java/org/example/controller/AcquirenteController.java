package org.example.controller;

import org.example.dto.response.CarrelloResponse;
import org.example.dto.response.EventoResponse;
import org.example.dto.response.OrdineResponse;
import org.example.dto.response.ProdottoResponse;
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

    //------GESTIONE CARRELLO------

    //visualizza il carrello
    @GetMapping("/carrello/{idAcquirente}")
    public ResponseEntity<?> visualizzaCarrello(@PathVariable Long idAcquirente) {
        try {
            var carrello = acquirenteService.getCarrello(idAcquirente);
            return ResponseEntity.ok(CarrelloResponse.fromEntity(carrello));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //aggiungi al carrello
    @PostMapping("/carrello/aggiungi")
    public ResponseEntity<?> aggiungiAlCarrello(@RequestParam Long idAcquirente, @RequestParam Long idProdotto) {
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

    //------GESTIONE ORDINE------

    //effettua ordine
    @PostMapping("/ordine/effettua")
    public ResponseEntity<?> effettuaOrdine(@RequestParam Long idAcquirente) {
        try{
            var ordine = acquirenteService.effettuaOrdine(idAcquirente);
            return ResponseEntity.ok(OrdineResponse.fromEntity(ordine));
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

    //------GESTIONE EVENTI LATO ACQUIRENTE------

    //prenota evento
    @PostMapping("/evento/prenota")
    public ResponseEntity<String> prenotaEvento(@RequestParam Long idAcquirente,
                                                @RequestParam Long idEvento,
                                                @RequestParam int numeroPosti) {
        try{
            acquirenteService.prenotaEvento(idAcquirente, idEvento, numeroPosti);
            return ResponseEntity.ok("Prenotazione confermata per " + numeroPosti + " posti");
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //annulla la prenotazione all'evento
    @DeleteMapping("/evento/annulla-prenotazione/{idPrenotazione}")
    public ResponseEntity<String> annullaPrenotazione(@PathVariable Long idPrenotazione, @RequestParam Long idAcquirente) {
        try{
            acquirenteService.annullaPrenotazione(idPrenotazione, idAcquirente);
            return ResponseEntity.ok("Prenotazione annullata");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}