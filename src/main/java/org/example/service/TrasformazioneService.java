package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.request.CertificazioneRequest;
import org.example.dto.request.CreaMetodoRequest;
import org.example.dto.request.CreaProcessoRequest;
import org.example.dto.request.ModificaProdottoSingoloRequest;
import org.example.model.*;
import org.example.model.state.StatoBozza;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrasformazioneService {

    @Autowired private TrasformazioneRepository trasformazioneRepo;
    @Autowired private UtenteRepository utenteRepo;
    @Autowired private ProdottoRepository prodottoRepo;
    @Autowired private MetodoTrasformazioneRepository metodoRepo;


    //visualizza materie prime disponibili
    public List<ProdottoSingolo> getMateriePrimeDisponibili(){

        return prodottoRepo.findByStatoNome("PUBBLICATO").stream()
                .filter(p -> p instanceof ProdottoSingolo)
                .map(p -> (ProdottoSingolo) p)
                .toList();
    }

    //crea il processo di trasformazione
    @Transactional
    public MetodoTrasformazione creaNuovoMetodo(CreaMetodoRequest request) {
        MetodoTrasformazione metodo = new MetodoTrasformazione(
                request.nome(),
                request.descrizione()
        );
        return metodoRepo.save(metodo);
    }

    //crea prodotto trasformato
    @Transactional
    public ProdottoSingolo eseguiTrasformazione(CreaProcessoRequest request) {
        Trasformatore trasformatore = (Trasformatore) utenteRepo.findById(request.idTrasformatore())
                .orElseThrow(()-> new RuntimeException("Trasformatore non trovato"));

        // 1. Recupero il Metodo
        MetodoTrasformazione metodo = metodoRepo.findById(request.idMetodo())
                .orElseThrow(() -> new RuntimeException("Metodo di trasformazione non trovato"));

        if (!"PUBBLICATO".equals(metodo.getStatoNome())) {
            throw new RuntimeException("Il metodo selezionato non è ancora stato approvato.");
        }

        // 2. Recupero Input e Scarico Magazzino
        List<ProdottoSingolo> inputList = new ArrayList<>();
        for(Integer id : request.idsProdottoInput()){
            Prodotto p = prodottoRepo.findById(Long.valueOf(id))
                    .orElseThrow(() -> new RuntimeException("Materia prima non trovata"));

            if(p instanceof ProdottoSingolo ps){
                if (ps.getQuantitaDisponibile() < 1) {
                    throw new RuntimeException("Materia prima esaurita: " + ps.getNome());
                }
                ps.setQuantitaDisponibile(ps.getQuantitaDisponibile() - 1);
                prodottoRepo.save(ps);

                inputList.add(ps);
            } else {
                throw new RuntimeException("Input non valido");
            }
        }

        // 3. Output
        ProdottoSingolo outputProdotto = new ProdottoSingolo(
                request.nomeOutput(), request.descrizioneOutput(),
                request.prezzoOutput(), request.quantitaOutput(), trasformatore
        );

        if(request.certificazioni() != null){
            for(CertificazioneRequest c : request.certificazioni()){
                outputProdotto.aggiungiCertificazione(new Certificazione(TipoCertificazione.valueOf(c.nome()), c.enteRilascio(), c.descrizione()));
            }
        }

        // 4. Salvo l'esecuzione
        ProcessoTrasformazione processo = new ProcessoTrasformazione(
                LocalDate.now(),
                metodo,
                trasformatore,
                inputList,
                List.of(outputProdotto)
        );

        trasformazioneRepo.save(processo);

        return outputProdotto;
    }

    //modifica prodotti
    @Transactional
    public ProdottoSingolo modificaProdotto(ModificaProdottoSingoloRequest request) {
        Prodotto p = prodottoRepo.findById(request.idProdotto())
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
        if (!p.getVenditore().getId().equals(request.idVenditore())) {
            throw new RuntimeException("Non puoi modificare un prodotto non tuo");
        }
        if (!(p instanceof ProdottoSingolo ps)) {
            throw new RuntimeException("Il prodotto non è modificabile");
        }

        ps.setNome(request.nome());
        ps.setDescrizione(request.descrizione());
        ps.setPrezzo(request.prezzo());

        if (request.certificazioni() != null) {
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
    public void eliminaProdotto(Long idProdotto, Long idTrasformatore) {
        Prodotto p = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if (!p.getVenditore().getId().equals(idTrasformatore)) {
            throw new RuntimeException("Non puoi eliminare un prodotto non tuo");
        }

        prodottoRepo.delete(p);
    }


    //visualizza i propri prodotti
    public List<Prodotto> getProdottiTrasformatore(Long idTrasformatore) {
        Venditore trasformatore = (Venditore) utenteRepo.findById(idTrasformatore)
                .orElseThrow(() -> new RuntimeException("Trasformatore non trovato"));
        return prodottoRepo.findByVenditore(trasformatore);
    }

    //manda in approvazione al curatore
    @Transactional
    public void richiediPubblicazione(Long idProdotto) {
        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
        prodotto.richiediApprovazione();
        prodottoRepo.save(prodotto);
    }
}

