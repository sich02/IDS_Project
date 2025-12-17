package org.example.service;

import org.example.dto.request.CreaProcessoRequest;
import org.example.dto.request.CertificazioneRequest;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrasformazioneService {
    @Autowired
    private TrasformazioneRepository trasformazioneRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private ProdottoRepository prodottoRepo;

    @Transactional
    public ProdottoSingolo creaProcessoTrasformazione(CreaProcessoRequest request) {
        Trasformatore trasformatore = (Trasformatore) utenteRepo.findById(request.idTrasformatore())
                .orElseThrow(()-> new RuntimeException("Trasformatore non trovato"));

        List<ProdottoSingolo> inputList = new ArrayList<>();
        for(Integer id : request.idsProdottoInput()){
            Prodotto p = prodottoRepo.findById(Long.valueOf(id)).orElseThrow();
            if(p instanceof  ProdottoSingolo ps){
                inputList.add(ps);
            }
        }

        //crea output
        ProdottoSingolo outputProdotto = new ProdottoSingolo(request.nomeOutput(), request.descrizioneOutput(),
                                                             request.prezzoOutput(), trasformatore);

        if(request.certificazioni() != null){
            for(CertificazioneRequest c : request.certificazioni()){
                outputProdotto.aggiungiCertificazione(new Certificazione(TipoCertificazione.valueOf(c.nome()), c.enteRilascio(), c.descrizione()));
            }
        }
        //registrazione del processo
        ProcessoTrasformazione processo = new ProcessoTrasformazione(
                LocalDate.now(),
                request.descrizioneProcesso(),
                trasformatore,
                inputList,
                List.of(outputProdotto)
        );

        trasformazioneRepo.save(processo);

        return outputProdotto;
    }

    public List<Prodotto> getProdottiTrasformatore(Long idTrasformatore) {
        Venditore trasformatore = (Venditore) utenteRepo.findById(idTrasformatore)
                .orElseThrow(() -> new RuntimeException("Trasformatore non trovato"));
        return prodottoRepo.findByVenditore(trasformatore);
    }

    @Transactional
    public void richiediPubblicazione(Long idProdotto) {
        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
        prodotto.richiediApprovazione();
        prodottoRepo.save(prodotto);
    }
}
