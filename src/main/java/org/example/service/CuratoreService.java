package org.example.service;

import org.example.model.Prodotto;
import org.example.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class CuratoreService {

    @Autowired private ProdottoRepository prodottoRepo;

    public List<Prodotto> getProdottiInRevisione() {
        return prodottoRepo.findByStatoNome("IN_APPROVAZIONE");
    }

    @Transactional
    public void approvaProdotto(Long idProdotto) {
        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // STATE PATTERN: Il cambio di stato e comportamento è delegato all'oggetto
        prodotto.pubblica();

        prodottoRepo.save(prodotto);
    }

    @Transactional
    public void rifiutaProdotto(Long idProdotto, String motivazione) {
        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // STATE PATTERN
        prodotto.rifiuta(motivazione);

        prodottoRepo.save(prodotto);
    }
}