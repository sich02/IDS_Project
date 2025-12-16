package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrasformazioneService {
    @Autowired
    private TrasformazioneRepository trasformazioneRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private ProdottoRepository prodottoRepo;

    @Transactional
    public ProdottoSingolo creaProcessoTrasformazione(Map<String, Object> dati){
        //identifico il trasformatore
        Long idTrasf = Long.valueOf(dati.get("idTrasformatore").toString());
        Trasformatore trasformatore = (Trasformatore) utenteRepo.findById(idTrasf)
                .orElseThrow(()-> new RuntimeException("Trasformatore non trovato"));

        //recupera e valida l'input
        List<Integer> idsInput = (List<Integer>) dati.get("idProdottoInput");
        if(idsInput == null || idsInput.isEmpty()){
            throw new IllegalArgumentException("Nessun prodotto input specificato.");
        }

        List<ProdottoSingolo> inputList = new ArrayList<>();
        for(Integer id : idsInput){
            Prodotto p = prodottoRepo.findById(Long.valueOf(id))
                    .orElseThrow(()-> new RuntimeException("Prodotto input non trovato" + id));

            if(p instanceof  ProdottoSingolo){
                inputList.add((ProdottoSingolo) p);
            }else{
                throw new IllegalArgumentException("Impossibile trasformare un pacchetto o altro contenuto.");
            }
        }

        //crea output
        String nomeOut = (String) dati.get("nomeOutput");
        String descOut = (String) dati.get("descrizioneOutput");
        double prezzoOut = Double.valueOf(dati.get("prezzoOutput").toString());

        ProdottoSingolo outputProdotto = new ProdottoSingolo(nomeOut, descOut, prezzoOut, trasformatore);

        //gestione certificazioni
        if(dati.containsKey("certificazioni")){
            List<Map<String, String>> certs = (List<Map<String, String>>) dati.get("certificazioni");
            for(Map<String, String> cMap:  certs){
                TipoCertificazione tipo = TipoCertificazione.valueOf(cMap.get("nome"));
                Certificazione c = new Certificazione(tipo, cMap.get("enteRilascio"), cMap.get("descrizione"));
                outputProdotto.aggiungiCertificazione(c);
            }
        }
        //registrazione del processo
        ProcessoTrasformazione processo = new ProcessoTrasformazione(
                LocalDate.now(),
                (String) dati.get("descrizione processo"),
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
