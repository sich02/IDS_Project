package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Certificazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCertificazione tipo;

    private String enteRilascio;
    private String descrizione;

    public Certificazione(TipoCertificazione tipo, String enteRilascio, String descrizione) {
        this.tipo = tipo;
        this.enteRilascio = enteRilascio;
        this.descrizione = descrizione;
    }
}
