package org.example.controller;
import org.example.model.Prodotto;
import org.example.model.Produttore;
import org.example.repository.ProdottoRepository;
import java.util.Map;
import java.util.List;

public class ProduttoreController {
    private ProdottoRepository prodottoRepo;

    public ProduttoreController(ProdottoRepository prodottoRepo) {
        this.prodottoRepo = prodottoRepo;
    }

    public Prodotto creaProdotto(Produttore produttore, Map<String, Object> dati) {
        String nome = (String) dati.get("nome");
        String descrizione = (String) dati.get("descrizione");
        double prezzo = (double) dati.get("prezzo");

        // Crea il prodotto (Stato iniziale = BOZZA di default nel costruttore)
        Prodotto nuovoProdotto = new Prodotto(nome, descrizione, prezzo, produttore);

        // Salva
        prodottoRepo.salva(nuovoProdotto);
        return nuovoProdotto;
    }

    public List<Prodotto> getMieiProdotti(Produttore produttore) {
        return prodottoRepo.trovaPerProduttore(produttore);
    }

    public void inviaInRevisione(Prodotto prodotto) {
        // Qui scatta il Pattern State: Bozza -> InApprovazione
        prodotto.richiediApprovazione();
        // Aggiorna lo stato nel DB
        prodottoRepo.salva(prodotto);
    }
}
