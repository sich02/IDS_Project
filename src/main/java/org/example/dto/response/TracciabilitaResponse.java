package org.example.dto.response;

import org.example.model.Pacchetto;
import org.example.model.Prodotto;
import org.example.model.ProdottoSingolo;
import org.example.model.Venditore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record TracciabilitaResponse(
        Long idProdotto,
        String nomeProdotto,
        String descrizione,
        String nomeVenditore,
        String tipoVenditore,
        List<String> certificazioni, //per gestire prodotti singoli
        List<TracciabilitaResponse> componenti //per gestire pacchetti
) {
    public static TracciabilitaResponse formEntity(Prodotto p){
        String nomeVenditore = "";
        String tipoVenditore = "";

        if(p.getVenditore()!=null){
            nomeVenditore = p.getVenditore().getNome() + " " +p.getVenditore().getCognome();
            if(p.getVenditore().getRuolo()!=null){
                tipoVenditore = p.getVenditore().getRuolo().toString();
            }
        }

        //gestione del prodotto singolo
        if(p instanceof ProdottoSingolo ps){
            List<String> certs= ps.getCertificazioni().stream()
                    .map(c -> c.getTipo().toString() + " (" + c.getEnteRilascio() + ") ").toList();

            return  new TracciabilitaResponse(
                    p.getId(), p.getNome(), p.getDescrizione(),
                    nomeVenditore, tipoVenditore,
                    certs,
                    Collections.emptyList()
            );
        }

        //gestione del pacchetto
        if(p instanceof Pacchetto pac){
            List<TracciabilitaResponse> listaComponenti = pac.getProdotti().stream()
                    .map(TracciabilitaResponse::formEntity).toList();

            return new TracciabilitaResponse(
                    p.getId(), p.getNome(), p.getDescrizione(),
                    nomeVenditore, tipoVenditore,
                    Collections.emptyList(),
                    listaComponenti
            );
        }
        return null;
    }
}
