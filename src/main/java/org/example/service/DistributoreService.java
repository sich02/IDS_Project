package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.dto.request.CreaPacchettoRequest;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributoreService {
    @Autowired
    private ProdottoRepository prodottoRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    //crea un pacchetto
    @Transactional
    public Pacchetto creaPacchetto(CreaPacchettoRequest request){
        Distributore distributore = (Distributore) utenteRepo.findById(request.idDistributore())
                .orElseThrow(()-> new RuntimeException("Utente non trovato"));

        Pacchetto pacchetto = new Pacchetto(request.nome(), request.descrizione(), 0.0, distributore);

        List<Prodotto> prodotti = prodottoRepo.findAllById(request.idsProdottoDaIncludere());
        if(prodotti.isEmpty()) throw new RuntimeException("Il pacchetto deve contenere almeno un prodotto");

        pacchetto.setProdotti(prodotti);

        return prodottoRepo.save(pacchetto);
    }

    //visualizza lista dei pacchetti
    public List<Prodotto> getPacchettiDistributore(Long idDistributore){
        Distributore d = (Distributore) utenteRepo.findById(idDistributore)
                .orElseThrow(()-> new RuntimeException("Distributore non trovato"));
        return prodottoRepo.findByVenditore(d);
    }

    //manda in approvazione
    @Transactional
    public void richiediPubblicazione(Long id){
        Prodotto p = prodottoRepo.findById(id).orElseThrow(()-> new RuntimeException("Pacchetto non trovato"));
        p.richiediApprovazione();
        prodottoRepo.save(p);
    }
}
