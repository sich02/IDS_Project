package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.state.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public abstract class Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;

    // Gestione Stato (Salvato come stringa, usato come Oggetto)
    private String statoNome;

    @Transient
    private StatoContenuto statoCorrente;

    public Contenuto(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.statoNome = "BOZZA";
        this.statoCorrente = new StatoBozza();
    }

    @PostLoad
    private void initStato() {
        if (this.statoNome == null) this.statoNome = "BOZZA";
        switch (this.statoNome) {
            case "BOZZA": this.statoCorrente = new StatoBozza(); break;
            case "IN_APPROVAZIONE": this.statoCorrente = new StatoInApprovazione(); break;
            case "PUBBLICATO": this.statoCorrente = new StatoPubblicato(); break;
            default: this.statoCorrente = new StatoBozza();
        }
    }

    // Metodi di Business delegati allo stato
    public void richiediApprovazione() {
        statoCorrente.inviaInRevisione(this);
        this.statoNome = statoCorrente.toString();
    }

    public void pubblica() {
        statoCorrente.approva(this);
        this.statoNome = statoCorrente.toString();
    }

    public void rifiuta(String motivazione) {
        statoCorrente.rifiuta(this, motivazione);
        this.statoNome = statoCorrente.toString();
    }

    public void setStato(StatoContenuto nuovoStato) {
        this.statoCorrente = nuovoStato;
        this.statoNome = nuovoStato.toString();
    }
}