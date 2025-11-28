package org.example.repository;
import org.example.model.Prodotto;
import org.example.model.Produttore;
import org.example.model.state.StatoProdotto;

import java.util.List;

public interface ProdottoRepository {
    void salva(Prodotto p);
    Prodotto trovaPerId(long id);
    List<Prodotto> trovaPerStato(StatoProdotto s); // per Curatore
    List<Prodotto> trovaPerProduttore(Produttore p); //per produttore
}
