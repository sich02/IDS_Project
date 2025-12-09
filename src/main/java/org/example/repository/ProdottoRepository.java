package org.example.repository;
import org.example.model.Prodotto;
import org.example.model.Venditore;
import org.example.model.state.StatoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface ProdottoRepository extends JpaRepository<Prodotto,Long> {

    List<Prodotto> findByVenditore(Venditore venditore);
    List<Prodotto> findByStatoProdotto(StatoProdotto statoProdotto);
}
