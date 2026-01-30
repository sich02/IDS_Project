package org.example.service;

import org.example.model.MetodoTrasformazione;
import org.example.model.Prodotto;
import org.example.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.example.repository.MetodoTrasformazioneRepository;
import java.util.List;

@Service
public class CuratoreService {

    @Autowired
    private ProdottoRepository prodottoRepo;
    @Autowired
    private MetodoTrasformazioneRepository metodoRepo;

    //gestione contenuti
    public List<Prodotto> getProdottiInRevisione() {

        return prodottoRepo.findByStatoNome("IN_APPROVAZIONE");
    }

    @Transactional
    public void approvaProdotto(Long idProdotto) {
        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        prodotto.pubblica();

        prodottoRepo.save(prodotto);
    }

    @Transactional
    public void rifiutaProdotto(Long idProdotto, String motivazione) {
        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        prodotto.rifiuta(motivazione);

        prodottoRepo.save(prodotto);
    }

    //gestione specifica per i metodi

    public List<MetodoTrasformazione> getMetodiInRevisione() {
        return metodoRepo.findByStatoNome("IN_APPROVAZIONE");
    }

    @Transactional
    public void approvaMetodo(Long idMetodo){
        MetodoTrasformazione metodo = metodoRepo.findById(idMetodo)
                .orElseThrow(() -> new RuntimeException("Metodo non trovato"));
        metodo.pubblica();
        metodoRepo.save(metodo);
    }

    @Transactional
    public void rifiutaMetodo(Long idMetodo, String motivazione) {
        MetodoTrasformazione metodo = metodoRepo.findById(idMetodo)
                .orElseThrow(() -> new RuntimeException("Metodo non trovato"));

        metodo.rifiuta(motivazione);
        metodoRepo.save(metodo);
    }
}