package org.example.service;

import org.example.model.Prodotto;
import org.example.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicService {
    @Autowired
    private ProdottoRepository prodottoRepo;

    //lista prodotti in stato pubblicato
    public List<Prodotto> getCatalogoCompleto() {
        return prodottoRepo.findAll().stream()
                .filter(p -> "PUBBLICATO".equals(p.getStatoNome())).toList();
    }

    //dettagli tracciabilità di un singolo prodotto
    public Prodotto getDettagliTracciabilita(long idProdotto) {
        Prodotto p = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if(!"PUBBLICATO".equals(p.getStatoNome())) {
            throw new RuntimeException("Prodotto non in stato PUBBLICATO");
        }

        return p;
    }
}
