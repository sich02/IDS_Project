package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Evento extends Contenuto {
    private LocalDateTime dataEvento;
    private String luogo;
    private int maxPartecipanti;
    private double prezzoBiglietto;

    @ManyToOne
    private Animatore organizzatore;

    public Evento(String nome, String descrizione, LocalDateTime dataEvento, String luogo,
                  int maxPartecipanti, double prezzoBiglietto, Animatore organizzatore) {
        super(nome, descrizione);
        this.dataEvento = dataEvento;
        this.luogo = luogo;
        this.maxPartecipanti = maxPartecipanti;
        this.prezzoBiglietto = prezzoBiglietto;
        this.organizzatore = organizzatore;
    }
}