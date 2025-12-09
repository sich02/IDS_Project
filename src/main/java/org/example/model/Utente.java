package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public abstract class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;

    @Column(unique = true) // La mail deve essere univoca nel DB
    private String email;

    private String password;

    @Enumerated(EnumType.STRING) // Salva il ruolo come testo ("PRODUTTORE") invece di numero
    private RuoloUtente ruolo;

    // Costruttore di utilità per creare oggetti velocemente nel codice
    public Utente(String nome, String cognome, String email, String password, RuoloUtente ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }
}
