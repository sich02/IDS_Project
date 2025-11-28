package org.example.controller;
import org.example.model.Prodotto;
import org.example.model.state.StatoInApprovazione;
import org.example.repository.ProdottoRepository;
import java.util.List;
public class CuratoreController {
    private ProdottoRepository prodottoRepo;

    public CuratoreController(ProdottoRepository prodottoRepo) {
        this.prodottoRepo = prodottoRepo;
    }

    public List<Prodotto> visualizzaCodaRevisioni() {
        // Chiede al repo tutti i prodotti nello stato "IN_APPROVAZIONE"
        return prodottoRepo.trovaPerStato(new StatoInApprovazione());
    }

    public void approvaProdotto(Prodotto prodotto) {
        // Pattern State: InApprovazione -> Pubblicato
        prodotto.pubblica();
        prodottoRepo.salva(prodotto);
    }

    public void rifiutaProdotto(Prodotto prodotto, String motivo) {
        // Pattern State: InApprovazione -> Bozza
        prodotto.rifiuta(motivo);
        prodottoRepo.salva(prodotto);
    }
}
