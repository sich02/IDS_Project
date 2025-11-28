package org.example.model;

public abstract class Utente {
    private static long idCounter = 0;
    protected long id;
    protected String nome;
    protected String cognome;
    protected String email;
    protected String password;
    protected RuoloUtente ruolo;

    protected Utente(){
        this.id = ++idCounter;
    }
    public Utente(String nome, String cognome, String email, String password, RuoloUtente ruolo){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    public long getId(){return id;}
    public void setId(long id){this.id = id;}

    public String getNome(){return nome;}
    public void setNome(String nome){this.nome = nome;}

    public String getCognome(){return cognome;}
    public void setCognome(String cognome){this.cognome = cognome;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public RuoloUtente getRuolo(){return ruolo;}
    public void setRuolo(RuoloUtente ruolo){this.ruolo = ruolo;}

    @Override
    public String toString(){
        return "Utente{" + "id=" + id + ", ruolo=" + ruolo + ", email='" + email + '\'' + '}';
    }

}
