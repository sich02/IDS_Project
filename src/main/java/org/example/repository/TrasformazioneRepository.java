package org.example.repository;

import org.example.model.ProcessoTrasformazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrasformazioneRepository extends JpaRepository<ProcessoTrasformazione, Long> {
}
