package org.example.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataCreazione;
    private double totale;
    private String stato; // ES: "CREATO", "PAGATO", "ANNULLATO"

    @ManyToOne
    private Acquirente acquirente;

    @ManyToMany // Snapshot dei prodotti acquistati
    private List<Prodotto> prodotti;

    public Ordine(Acquirente acquirente, List<Prodotto> prodotti, double totale) {
        this.acquirente = acquirente;
        this.prodotti = prodotti;
        this.totale = totale;
        this.dataCreazione = LocalDateTime.now();
        this.stato = "CREATO";
    }
}
