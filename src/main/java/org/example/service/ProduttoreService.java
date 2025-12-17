package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.request.CertificazioneRequest;
import org.example.dto.request.CreaProdottoRequest;
import org.example.model.*;
import org.example.repository.ProdottoRepository;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduttoreService {

    @Autowired private ProdottoRepository prodottoRepo;
    @Autowired private UtenteRepository utenteRepo;

    // Crea un prodotto singolo
    @Transactional
    public ProdottoSingolo creaProdotto(CreaProdottoRequest request) {
        Produttore produttore = (Produttore) utenteRepo.findById(request.idProduttore())
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        ProdottoSingolo p = new ProdottoSingolo(request.nome(), request.descrizione(), request.prezzo(),  produttore);

        if(request.certificazioni() !=null){
            for(CertificazioneRequest cReq : request.certificazioni()){
                TipoCertificazione tipo = TipoCertificazione.valueOf(cReq.nome());
                p.aggiungiCertificazione(new Certificazione(tipo, cReq.enteRilascio(), cReq.descrizione()));
            }
        }
        return prodottoRepo.save(p);
    }

    // Crea un pacchetto
    @Transactional
    public Pacchetto creaPacchetto(Long idProduttore, String nome, String descrizione, List<Long> idsProdottiDaIncludere) {
        Produttore produttore = (Produttore) utenteRepo.findById(idProduttore)
                .orElseThrow(() -> new RuntimeException("Produttore non trovato"));

        Pacchetto pacchetto = new Pacchetto(nome, descrizione, 0.0, produttore); // Il prezzo sarà calcolato o settato

        List<Prodotto> prodotti = prodottoRepo.findAllById(idsProdottiDaIncludere);
        if(prodotti.isEmpty()) throw new RuntimeException("Un pacchetto deve contenere almeno un prodotto");

        pacchetto.setProdotti(prodotti);
        return prodottoRepo.save(pacchetto);
    }

    //visualizza i prodotti
    public List<Prodotto> getIMieiProdotti(Long idProduttore) {
        Produttore p = (Produttore) utenteRepo.findById(idProduttore).orElseThrow();
        return prodottoRepo.findByVenditore(p);
    }

    //richiedi la pubblicazione
    public void richiediPubblicazione(Long id){
        Prodotto p = prodottoRepo.findById(id).orElseThrow();
        p.richiediApprovazione();
        prodottoRepo.save(p);
    }
}