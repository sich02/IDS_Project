package org.example.controller;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/trasfomatore")
public class TrasformatoreController {
    @Autowired
    private TrasformazioneRepository trasformazioneRepo;
    @Autowired
    private UtenteRepository utenteRepo;
    @Autowired
    private ProdottoRepository prodottoRepo;

    @PostMapping("/crea-processo")
    public ResponseEntity<?> creaProcesso(@RequestBody Map<String, Object> dati) {
        try{
            Long idTrasf = Long.valueOf(dati.get("idTrasformatore").toString());
            Trasformatore trasformatore =  (Trasformatore) utenteRepo.findById(idTrasf).
                    orElseThrow(()-> new RuntimeException("Trasformatore non trovato"));

            //input dei prodotti
            List<Integer> idsInput = (List<Integer>) dati.get("idsProdottoInput");
            List<ProdottoSingolo> inputlist = new ArrayList<>();
            for(Integer id : idsInput){
                Prodotto p = prodottoRepo.findById(Long.valueOf(id))
                        .orElseThrow(()-> new RuntimeException("Input non trovato: "+id));
                if(p instanceof ProdottoSingolo){
                    inputlist.add((ProdottoSingolo) p);
                }
            }

            //creazione nuovo prodotto
            String nomeOut = (String) dati.get("nomeOutput");
            String descOut = (String) dati.get("descrizioneOutput");
            double prezzoOut = Double.valueOf(dati.get("prezzoOutput").toString());

            ProdottoSingolo outputProdotto = new ProdottoSingolo(nomeOut, descOut, prezzoOut, trasformatore);

            //aggiunta certificazioni
            if(dati.containsKey("certificazioni")){
                List<Map<String, String>> certs = (List<Map<String, String>>) dati.get("certificazioni");
                for(Map<String, String> cMap : certs){
                    TipoCertificazione tipoEnum = TipoCertificazione.valueOf(cMap.get("nome"));
                    Certificazione c = new Certificazione(
                            tipoEnum, cMap.get("enteRilascio"), cMap.get("descrizione")
                    );
                    outputProdotto.aggiungiCertificazione(c);
                }
            }

            prodottoRepo.save(outputProdotto);

            //registra il processo
            ProcessoTrasformazione processo = new  ProcessoTrasformazione(
                    LocalDate.now(),
                    (String) dati.get("descrizioneProcesso"),
                    trasformatore,
                    inputlist,
                    List.of(outputProdotto)
            );

            return ResponseEntity.ok("Trasformazione completata. Nuovo prodotto creato: " + outputProdotto.getNome());

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore: non trovato");
        }
    }
}
