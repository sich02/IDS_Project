package org.example.repository;

import org.example.model.Acquirente;
import org.example.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByAcquirente(Acquirente acquirente);

    @Query("SELECT DISTINCT o FROM Ordine o JOIN o.prodotti p WHERE p.venditore.id = :idVenditore")
    List<Ordine> findOrdiniByVenditoreId(@Param("idVenditore") Long idVenditore);
}
