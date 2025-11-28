package org.example.model;
import org.example.model.state.StatoBozza;
import org.example.model.state.StatoProdotto;
public class Prodotto {
    private static long idCounter = 0;

    private long id;
    private String nome;
    private String descrizione;
    private double prezzo;


    private Produttore produttore;


    private StatoProdotto statoCorrente;


    public Prodotto() {
        this.id = ++idCounter;
        this.statoCorrente = new StatoBozza();
    }

    public Prodotto(String nome, String descrizione, double prezzo, Produttore produttore) {
        this();
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.produttore = produttore;
    }



    public void richiediApprovazione() {
        statoCorrente.inviaInRevisione(this);
    }

    public void pubblica() {
        statoCorrente.approva(this);
    }

    public void rifiuta(String motivazione) {
        statoCorrente.rifiuta(this, motivazione);
    }

    public void setStato(StatoProdotto nuovoStato) {
        this.statoCorrente = nuovoStato;
    }



    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public Produttore getProduttore() { return produttore; }
    public void setProduttore(Produttore produttore) { this.produttore = produttore; }

    public StatoProdotto getStatoCorrente() { return statoCorrente; }

    public String getStatoString() {
        return statoCorrente.toString();
    }
}
