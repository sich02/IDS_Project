package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pacchetto extends Prodotto {

    private double sconto;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pacchetto_contenuto",
            joinColumns = @JoinColumn(name = "pacchetto_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti = new ArrayList<>();

    public Pacchetto(String nome, String descrizione, double sconto, Venditore venditore) {
        super(nome, descrizione, 0.0, 1, venditore);
        this.sconto = sconto;
    }

    @Override
    public double getPrezzo() {
        if (this.prodotti == null || this.prodotti.isEmpty()) {
            return 0.0;
        }

        double somma = prodotti.stream()
                .mapToDouble(Prodotto::getPrezzo)
                .sum();

        if (sconto > 0) {
            return somma - (somma * (sconto / 100.0));
        }

        return somma;
    }
}