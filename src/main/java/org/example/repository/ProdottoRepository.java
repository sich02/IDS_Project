package org.example.repository;

import org.example.model.Prodotto;
import org.example.model.Venditore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    // Trova i prodotti di un venditore specifico
    List<Prodotto> findByVenditore(Venditore venditore);

    // Trova per nome dello stato (es. "IN_APPROVAZIONE")
    // Corrisponde al campo 'statoNome' definito nella classe padre Contenuto
    List<Prodotto> findByStatoNome(String statoNome);
}