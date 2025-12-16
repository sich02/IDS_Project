package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class AcquirenteService {

    @Autowired private UtenteRepository utenteRepo;
    @Autowired private ProdottoRepository prodottoRepo;
    @Autowired private CarrelloRepository carrelloRepo;
    @Autowired private OrdineRepository ordineRepo;
    @Autowired private EventoRepository eventoRepo;
    @Autowired private PrenotazioneRepository prenotazioneRepo;

    @Transactional
    public void aggiungiAlCarrello(Long idAcquirente, Long idProdotto) {
        Acquirente acquirente = (Acquirente) utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Business Rule: Si possono comprare solo prodotti pubblicati
        if (!"PUBBLICATO".equals(prodotto.getStatoNome())) {
            throw new RuntimeException("Prodotto non disponibile per l'acquisto.");
        }

        Carrello carrello = carrelloRepo.findByAcquirente(acquirente)
                .orElse(new Carrello(acquirente));

        carrello.getProdotti().add(prodotto);
        carrelloRepo.save(carrello);
    }

    @Transactional
    public Ordine effettuaOrdine(Long idAcquirente) {
        Acquirente acquirente = (Acquirente) utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Carrello carrello = carrelloRepo.findByAcquirente(acquirente)
                .orElseThrow(() -> new RuntimeException("Carrello vuoto"));

        if (carrello.getProdotti().isEmpty()) throw new RuntimeException("Carrello vuoto");

        double totale = carrello.getProdotti().stream().mapToDouble(Prodotto::getPrezzo).sum();

        // Crea Ordine
        Ordine ordine = new Ordine(acquirente, new ArrayList<>(carrello.getProdotti()), totale);
        ordineRepo.save(ordine);

        // Svuota Carrello (Atomicità garantita da @Transactional)
        carrello.getProdotti().clear();
        carrelloRepo.save(carrello);

        return ordine;
    }

    // ... qui aggiungi i metodi per rimuoviDalCarrello e prenotaEvento simili ai precedenti
}