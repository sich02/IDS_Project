package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.dto.request.CreaPacchettoRequest;
import org.example.dto.request.ModificaPacchettoRequest;
import org.example.model.*;
import org.example.model.state.StatoBozza;
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

    //modifica un pacchetto
    @Transactional
    public Pacchetto modificaPacchetto(ModificaPacchettoRequest request) {
        Prodotto p = prodottoRepo.findById(request.idPacchetto())
                .orElseThrow(()-> new RuntimeException("Prodotto non trovato"));

        if(!p.getVenditore().getId().equals(request.idDistributore())) {
            throw new RuntimeException("Non può essere modicato un pacchetto non proprio");
        }
        if(!(p instanceof Pacchetto pacchetto)) {
            throw new RuntimeException("L'id non corrisponde a nessun pacchetto");
        }

        pacchetto.setNome(request.nome());
        pacchetto.setDescrizione(request.descrizione());
        pacchetto.setSconto(request.sconto());

        if(request.idsProdottoDaIncludere() != null && !request.idsProdottoDaIncludere().isEmpty() ) {
            List<Prodotto> nuoviProdotti = prodottoRepo.findAllById(request.idsProdottoDaIncludere());
            if(nuoviProdotti.isEmpty()) throw new RuntimeException("Deve essere incluso almeno un prodotto valido");
            pacchetto.setProdotti(nuoviProdotti);
        }

        pacchetto.setStato(new StatoBozza());
        return prodottoRepo.save(pacchetto);
    }

    //elimina pacchetto
    @Transactional
    public void eliminaPacchetto(Long idPacchetto, Long idDistributore){
        Prodotto p = prodottoRepo.findById(idPacchetto)
                .orElseThrow(()-> new RuntimeException("Prodotto non trovato"));

        if(!p.getVenditore().getId().equals(idDistributore)) {
            throw new RuntimeException("Non puoi eliminare un pacchetto non tuo");
        }
        if (!(p instanceof Pacchetto)){
            throw new RuntimeException("L'elemento non è un pacchetto");
        }
        prodottoRepo.delete(p);
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
