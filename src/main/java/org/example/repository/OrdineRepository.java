package org.example.repository;

import org.springframework.stereotype.Repository;
import org.example.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
}
