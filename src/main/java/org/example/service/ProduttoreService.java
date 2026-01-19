package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.request.CertificazioneRequest;
import org.example.dto.request.CreaProdottoRequest;
import org.example.dto.request.ModificaProdottoSingoloRequest;
import org.example.model.*;
import org.example.model.state.StatoBozza;
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

    //modifica un prodotto già caricato
    @Transactional
    public ProdottoSingolo modificaProdotto(ModificaProdottoSingoloRequest request) {
        Prodotto p = prodottoRepo.findById(request.idProdotto()).orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if(!p.getVenditore().getId().equals(request.idVenditore())){
            throw new RuntimeException("Non puoi modificare un prodotto che non ti appartiene");
        }
        if(!(p instanceof ProdottoSingolo ps)){
            throw new RuntimeException("Il prodotto scelto non è un prodotto singolo");
        }

        ps.setNome(request.nome());
        ps.setDescrizione(request.descrizione());
        ps.setPrezzo(request.prezzo());

        if(request.certificazioni() !=null){
            ps.getCertificazioni().clear();
            for (CertificazioneRequest cReq : request.certificazioni()) {
                TipoCertificazione tipo = TipoCertificazione.valueOf(cReq.nome());
                ps.aggiungiCertificazione(new Certificazione(tipo, cReq.enteRilascio(), cReq.descrizione()));
            }
        }

        ps.setStato(new StatoBozza());
        return prodottoRepo.save(ps);
    }

    //elimina prodotto
    @Transactional
    public void  eliminaProdotto(Long idProdotto, Long idProduttore){
        Prodotto p = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if(!p.getVenditore().getId().equals(idProduttore)){
            throw new RuntimeException("Non puoi elimiare un prodotto non tuo");
        }
        prodottoRepo.delete(p);
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