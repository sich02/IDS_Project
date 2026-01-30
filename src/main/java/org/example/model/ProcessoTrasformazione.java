package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ProcessoTrasformazione{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataTrasformazione;

    @ManyToOne
    private MetodoTrasformazione metodo;

    @ManyToOne
    private Trasformatore trasformatore;

    @ManyToMany
    private List<ProdottoSingolo> prodottiInput;

    @OneToMany
    private List<ProdottoSingolo> prodottiOutput;

    public ProcessoTrasformazione(LocalDate dataTrasformazione,MetodoTrasformazione metodo,
                                  Trasformatore trasformatore,
                                  List<ProdottoSingolo> prodottiInput,
                                  List<ProdottoSingolo> prodottiOutput){
        this.dataTrasformazione = dataTrasformazione;
        this.metodo = metodo;
        this.trasformatore = trasformatore;
        this.prodottiInput = prodottiInput;
        this.prodottiOutput = prodottiOutput;
    }
}
