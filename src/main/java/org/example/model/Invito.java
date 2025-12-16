package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Invito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Evento evento;

    @ManyToOne
    private Venditore venditore;

    private boolean accettato;

    public Invito(Evento evento, Venditore venditore){
        this.evento = evento;
        this.venditore = venditore;
        this.accettato = false;
    }
}
