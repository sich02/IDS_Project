package org.example.model;

public class Acquirente extends Utente{
    private String indirizzoSpedizione;

    public Acquirente(){
        super();
        this.ruolo = RuoloUtente.ACQUIRENTE;
    }

    public Acquirente(String nome, String cognome, String email, String password, String indirizzoSpedizione){
        super(nome, cognome, email, password, RuoloUtente.ACQUIRENTE);
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    public String getIndirizzoSpedizione() {return indirizzoSpedizione;}
    public void setIndirizzoSpedizione(String indirizzoSpedizione) {this.indirizzoSpedizione = indirizzoSpedizione;}
}
