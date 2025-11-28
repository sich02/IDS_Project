package org.example.model;

public class Curatore extends Utente{
    private String matricola;

    public Curatore(){
        super();
        this.ruolo = RuoloUtente.CURATORE;
    }
    public Curatore(String nome, String cognome, String email, String password, String matricola){
        super(nome, cognome, email, password, RuoloUtente.CURATORE);
        this.matricola = matricola;
    }

    public String getMatricola(){return matricola;}
    public void setMatricola(String matricola){this.matricola = matricola;}

}
