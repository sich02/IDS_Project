package org.example.controller;

import org.example.dto.response.AziendaResponse;
import org.example.dto.response.EventoResponse;
import org.example.dto.response.ProdottoResponse;
import org.example.dto.response.TracciabilitaResponse;
import org.example.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    @Autowired
    private PublicService publicService;

    //visualizzazione del catalogo
    @GetMapping("/catalogo")
    public ResponseEntity<List<ProdottoResponse>> getCatalogo() {
        var prodotti = publicService.getCatalogoCompleto();
        return  ResponseEntity.ok(prodotti.stream()
                .map(ProdottoResponse::fromEntity).toList());
    }

    //tracciabilita del prodotto
    @GetMapping("/storia/{idProdotto}")
    public ResponseEntity<?> getProdotto(@PathVariable Long idProdotto) {
        try{
            var prodotto = publicService.getDettagliTracciabilita(idProdotto);
            return  ResponseEntity.ok(TracciabilitaResponse.formEntity(prodotto));
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //visualizza la lista degli eventi disponibili
    @GetMapping("/eventi")
    public ResponseEntity<List<EventoResponse>> getEventi() {
        var eventi = publicService.getEventiPubblici();
        return  ResponseEntity.ok(eventi.stream()
                .map(EventoResponse::fromEntity)
                .toList());
    }

    //sistemi OSM
    @GetMapping("/aziende")
    public ResponseEntity<List<AziendaResponse>> getAziendeOSM() {
        try{
            var aziende = publicService.getAziendaFiliera();

            return ResponseEntity.ok(aziende.stream()
                    .map(AziendaResponse::fromEntity).toList());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
