package org.example.repository;

import org.example.model.MetodoTrasformazione;
import org.example.model.Trasformatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetodoTrasformazioneRepository extends JpaRepository<MetodoTrasformazione, Long>{
    List<MetodoTrasformazione> findByStatoNome(String statoNome);
    List<MetodoTrasformazione> findByAutore(Trasformatore autore);

}
