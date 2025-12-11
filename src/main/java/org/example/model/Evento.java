package org.example.model;

import jakarta.persistence.Entity;
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

    public Evento(String nome, String descrizione, LocalDateTime dataEvento, String luogo, int maxPartecipanti, double prezzoBiglietto) {
        super(nome, descrizione);
        this.dataEvento = dataEvento;
        this.luogo = luogo;
        this.maxPartecipanti = maxPartecipanti;
        this.prezzoBiglietto = prezzoBiglietto;
    }
}