package org.example.controller;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/acquirente")
public class AcquirenteController {

    @Autowired private ProdottoRepository prodottoRepo;
    @Autowired private UtenteRepository utenteRepo;
    @Autowired private CarrelloRepository carrelloRepo;
    @Autowired private OrdineRepository ordineRepo;
    @Autowired private EventoRepository eventoRepo;
    @Autowired private PrenotazioneRepository prenotazioneRepo;

    // --- CARRELLO

    @PostMapping("/carrello/aggiungi")
    @Transactional
    public ResponseEntity<String> aggiungiAlCarrello(@RequestParam Long idAcquirente, @RequestParam Long idProdotto) {
        Acquirente acquirente = getAcquirente(idAcquirente);
        if (acquirente == null) return ResponseEntity.badRequest().body("Acquirente non trovato.");

        Prodotto prodotto = prodottoRepo.findById(idProdotto).orElse(null);
        if (prodotto == null || !"PUBBLICATO".equals(prodotto.getStatoNome())) {
            return ResponseEntity.badRequest().body("Prodotto non disponibile.");
        }

        // Recupera o crea il carrello
        Carrello carrello = carrelloRepo.findByAcquirente(acquirente)
                .orElse(new Carrello(acquirente));

        carrello.getProdotti().add(prodotto);
        carrelloRepo.save(carrello);

        return ResponseEntity.ok("Prodotto aggiunto al carrello: " + prodotto.getNome());
    }

    @DeleteMapping("/carrello/rimuovi")
    @Transactional
    public ResponseEntity<String> rimuoviDalCarrello(@RequestParam Long idAcquirente, @RequestParam Long idProdotto) {
        Acquirente acquirente = getAcquirente(idAcquirente);
        if (acquirente == null) return ResponseEntity.badRequest().body("Acquirente non trovato.");

        Carrello carrello = carrelloRepo.findByAcquirente(acquirente).orElse(null);
        if (carrello == null || carrello.getProdotti().isEmpty()) {
            return ResponseEntity.badRequest().body("Il carrello è vuoto.");
        }

        // Rimuove la prima occorrenza del prodotto
        boolean rimosso = carrello.getProdotti().removeIf(p -> p.getId().equals(idProdotto));

        if (rimosso) {
            carrelloRepo.save(carrello);
            return ResponseEntity.ok("Prodotto rimosso dal carrello.");
        } else {
            return ResponseEntity.badRequest().body("Prodotto non presente nel carrello.");
        }
    }

    // --- ORDINI ---

    @PostMapping("/ordine/effettua")
    @Transactional
    public ResponseEntity<String> effettuaOrdine(@RequestParam Long idAcquirente) {
        Acquirente acquirente = getAcquirente(idAcquirente);
        if (acquirente == null) return ResponseEntity.badRequest().body("Acquirente non trovato.");

        Carrello carrello = carrelloRepo.findByAcquirente(acquirente).orElse(null);
        if (carrello == null || carrello.getProdotti().isEmpty()) {
            return ResponseEntity.badRequest().body("Impossibile ordinare: il carrello è vuoto.");
        }

        // Calcolo totale
        double totale = carrello.getProdotti().stream().mapToDouble(Prodotto::getPrezzo).sum();

        // Crea l'ordine (copiando la lista dei prodotti)
        Ordine ordine = new Ordine(acquirente, new ArrayList<>(carrello.getProdotti()), totale);
        ordineRepo.save(ordine);

        // Svuota il carrello
        carrello.getProdotti().clear();
        carrelloRepo.save(carrello);

        return ResponseEntity.ok("Ordine effettuato con successo! ID Ordine: " + ordine.getId() + " - Totale: " + totale + "€");
    }

    @PutMapping("/ordine/annulla/{idOrdine}")
    public ResponseEntity<String> annullaOrdine(@PathVariable Long idOrdine, @RequestParam Long idAcquirente) {
        Ordine ordine = ordineRepo.findById(idOrdine).orElse(null);
        if (ordine == null) return ResponseEntity.badRequest().body("Ordine non trovato.");

        // Controllo che l'ordine appartenga all'utente e sia annullabile
        if (!ordine.getAcquirente().getId().equals(idAcquirente)) {
            return ResponseEntity.status(403).body("Non sei autorizzato ad annullare questo ordine.");
        }

        if (!"CREATO".equals(ordine.getStato())) {
            return ResponseEntity.badRequest().body("Impossibile annullare l'ordine. Stato attuale: " + ordine.getStato());
        }

        ordine.setStato("ANNULLATO");
        ordineRepo.save(ordine);

        return ResponseEntity.ok("Ordine annullato con successo.");
    }

    // --- EVENTI ---

    @PostMapping("/evento/prenota")
    public ResponseEntity<String> prenotaEvento(@RequestParam Long idAcquirente, @RequestParam Long idEvento) {
        Acquirente acquirente = getAcquirente(idAcquirente);
        if (acquirente == null) return ResponseEntity.badRequest().body("Acquirente non trovato.");

        Evento evento = eventoRepo.findById(idEvento).orElse(null);
        if (evento == null || !"PUBBLICATO".equals(evento.getStatoNome())) {
            return ResponseEntity.badRequest().body("Evento non disponibile.");
        }

        // Crea prenotazione
        Prenotazione prenotazione = new Prenotazione(acquirente, evento);
        prenotazioneRepo.save(prenotazione);

        return ResponseEntity.ok("Prenotazione confermata per l'evento: " + evento.getNome());
    }

    // --- Helper ---
    private Acquirente getAcquirente(Long id) {
        return utenteRepo.findById(id)
                .filter(u -> u instanceof Acquirente)
                .map(u -> (Acquirente) u)
                .orElse(null);
    }
}