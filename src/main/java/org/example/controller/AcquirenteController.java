package org.example.controller;

import org.example.model.Acquirente;
import org.example.model.Prodotto;
import org.example.model.Utente;
import org.example.repository.ProdottoRepository;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/acquirente")
public class AcquirenteController {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Endpoint 1: VISUALIZZA CATALOGO
     * Restituisce la lista dei prodotti che sono stati approvati dal Curatore.
     * Use Case: L'acquirente vuole vedere cosa può comprare.
     * URL: GET /api/acquirente/catalogo
     */
    @GetMapping("/catalogo")
    public ResponseEntity<List<Prodotto>> visualizzaCatalogo() {
        // Recuperiamo solo i prodotti nello stato "PUBBLICATO"
        // Questo sfrutta il metodo findByStatoNome che abbiamo visto nel Repository
        List<Prodotto> catalogo = prodottoRepository.findByStatoNome("PUBBLICATO");
        return ResponseEntity.ok(catalogo);
    }

    /**
     * Endpoint 2: ACQUISTA PRODOTTO
     * Simula l'acquisto di un prodotto da parte di un acquirente.
     * URL: POST /api/acquirente/acquista
     */
    @PostMapping("/acquista")
    public ResponseEntity<String> acquistaProdotto(@RequestParam Long idAcquirente, @RequestParam Long idProdotto) {

        // 1. Verifica che l'utente esista
        Optional<Utente> utenteOpt = utenteRepository.findById(idAcquirente);
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Utente non trovato.");
        }

        // 2. Verifica che l'utente sia effettivamente un Acquirente
        // (Controllo di sicurezza semplice basato sul tipo di classe)
        if (!(utenteOpt.get() instanceof Acquirente)) {
            return ResponseEntity.badRequest().body("Errore: L'utente specificato non è un Acquirente.");
        }

        // 3. Verifica che il prodotto esista
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(idProdotto);
        if (prodottoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Errore: Prodotto non trovato.");
        }

        Prodotto prodotto = prodottoOpt.get();

        // 4. Verifica che il prodotto sia acquistabile (cioè PUBBLICATO)
        // Usiamo il metodo getStatoNome() ereditato da Contenuto
        if (!"PUBBLICATO".equals(prodotto.getStatoNome())) {
            return ResponseEntity.badRequest().body("Errore: Il prodotto non è disponibile per l'acquisto (Stato: " + prodotto.getStatoNome() + ")");
        }

        // 5. Esecuzione dell'acquisto (Simulazione)
        // Qui in futuro potremmo creare un oggetto "Ordine" e salvarlo nel DB.
        return ResponseEntity.ok("Acquisto effettuato con successo! Hai comprato: " + prodotto.getNome() +
                " al prezzo di " + prodotto.getPrezzo() + "€");
    }
}