package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class ProduttoreService {

    @Autowired private ProdottoRepository prodottoRepo;
    @Autowired private UtenteRepository utenteRepo;

    // Crea un prodotto singolo
    @Transactional
    public ProdottoSingolo creaProdottoSingolo(Long idProduttore, String nome, String descrizione, double prezzo) {
        Produttore produttore = (Produttore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        ProdottoSingolo p = new ProdottoSingolo(nome, descrizione, prezzo, produttore);
        return prodottoRepo.save(p);
    }

    // Crea un pacchetto (COMPOSITE PATTERN: Aggregazione)
    @Transactional
    public Pacchetto creaPacchetto(Long idProduttore, String nome, String descrizione, List<Long> idsProdottiDaIncludere) {
        Produttore produttore = (Produttore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        Pacchetto pacchetto = new Pacchetto(nome, descrizione, 0.0, produttore); // Il prezzo sarà calcolato o settato

        // Recupera i prodotti figli e li aggiunge al pacchetto
        List<Prodotto> prodotti = prodottoRepo.findAllById(idsProdottiDaIncludere);
        if(prodotti.isEmpty()) throw new RuntimeException("Un pacchetto deve contenere almeno un prodotto");

        pacchetto.setProdotti(prodotti);

        // Opzionale: Se il Composite calcola il prezzo automatico, non serve settarlo.
        // Se invece è un prezzo scontato fisso, lo passi nel costruttore.

        return prodottoRepo.save(pacchetto);
    }

    public List<Prodotto> getIMieiProdotti(Long idProduttore) {
        Produttore p = (Produttore) utenteRepo.findById(idProduttore).orElseThrow();
        return prodottoRepo.findByVenditore(p);
    }
}