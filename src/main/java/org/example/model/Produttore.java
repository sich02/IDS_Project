package org.example.model;

import java.util.List;
import java.util.ArrayList;

public class Produttore extends Utente{
    private String partitaIva;
    private String ragioneSociale;
    private String indirizzoSede;

    //lista prodotti caricati
    private List<Prodotto> prodottiInseriti = new ArrayList<>();

    public Produttore(){
        super();
        this.ruolo = RuoloUtente.PRODUTTORE;
    }

    public Produttore(String nome, String cognome, String email, String password,
                      String partitaIva, String ragioneSociale, String indirizzoSede){
        super(nome, cognome, email, password,  RuoloUtente.PRODUTTORE);
        this.partitaIva = partitaIva;
        this.ragioneSociale = ragioneSociale;
        this.indirizzoSede = indirizzoSede;
    }

    public String getPartitaIva() {return partitaIva;}
    public void setPartitaIva(String partitaIva){this.partitaIva = partitaIva;}

    public String getRagioneSociale() {return ragioneSociale;}
    public void  setRagioneSociale(String ragioneSociale){this.ragioneSociale = ragioneSociale;}

    public String getIndirizzoSede() {return indirizzoSede;}
    public void setIndirizzoSede(String indirizzoSede) {this.indirizzoSede = indirizzoSede;}

    public List<Prodotto> getProdottiInseriti() {return prodottiInseriti;}
    public void setProdottiInseriti(List<Prodotto> prodottiInseriti){this.prodottiInseriti = prodottiInseriti;}
}
