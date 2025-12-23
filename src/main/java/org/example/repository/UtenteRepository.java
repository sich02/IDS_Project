package org.example.repository;

import  org.example.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository

public interface UtenteRepository extends JpaRepository<Utente,Long> {

    Optional<Utente> findByEmail(String email);

    boolean existsByEmail(String email);

    Long id(Long id);
}
