package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Crea tabelle separate collegate per ID
@Data // Lombok: Genera Getter, Setter, toString, equals in automatico
@NoArgsConstructor // Lombok: Genera costruttore vuoto obbligatorio per JPA
public abstract class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING) 
    private RuoloUtente ruolo;

    public Utente(String nome, String cognome, String email, String password, RuoloUtente ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }
}
