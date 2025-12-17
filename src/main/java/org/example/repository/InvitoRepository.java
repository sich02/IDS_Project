package org.example.repository;

import org.example.model.Evento;
import org.example.model.Invito;
import org.example.model.Venditore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface InvitoRepository extends JpaRepository<Invito,Long> {
    List<Invito> findByEvento(Evento evento);

    List<Invito> findByVenditore(Venditore venditore);
}
