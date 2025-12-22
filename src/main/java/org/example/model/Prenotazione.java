package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataPrenotazione;
    private int numeroPosti;

    @ManyToOne
    private Acquirente acquirente;

    @ManyToOne
    private Evento evento;

    public Prenotazione(Acquirente acquirente, Evento evento, int numeroPosti) {
        this.acquirente = acquirente;
        this.evento = evento;
        this.numeroPosti = numeroPosti;
        this.dataPrenotazione = LocalDateTime.now();
    }
}