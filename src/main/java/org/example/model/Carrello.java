package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Acquirente acquirente;

    @ManyToMany // Un prodotto può essere in più carrelli
    private List<Prodotto> prodotti = new ArrayList<>();

    public Carrello(Acquirente acquirente) {
        this.acquirente = acquirente;
    }
}
