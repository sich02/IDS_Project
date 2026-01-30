package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenditoreService {
    @Autowired
    private InvitoRepository invitoRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private OrdineRepository ordineRepo;

    @Autowired
    private ProdottoRepository prodottoRepo;

    public List<Invito> getMieiInviti(Long idVenditore) {
        Utente utente = utenteRepo.findById(idVenditore)
                .orElseThrow(()-> new RuntimeException("Utente non trovato"));

        if(!(utente instanceof Venditore)){
            throw new RuntimeException("L'utente specifico non è un venditore");
        }

        return invitoRepo.findByVenditore((Venditore) utente);
    }

    @Transactional
    public void gestisciInvito(Long idInvito, boolean accetta) {
        Invito invito = invitoRepo.findById(idInvito)
                .orElseThrow(()-> new EntityNotFoundException("Invito non trovato"));

        invito.setAccettato(accetta);
        invitoRepo.save(invito);

    }

    public List<Ordine> getOrdiniRicevuti(Long idVenditore) {
        Utente utente = utenteRepo.findById(idVenditore)
                .orElseThrow(() -> new RuntimeException("Venditore non trovato"));

        if (!(utente instanceof Venditore)) {
            throw new RuntimeException("L'utente non è un venditore");
        }

        return ordineRepo.findOrdiniByVenditoreId(idVenditore);
    }

    @Transactional
    public void spedisciOrdine(Long idOrdine, Long idVenditore) {
        Ordine ordine = ordineRepo.findById(idOrdine)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        boolean isMioOrdine = ordine.getProdotti().stream()
                .anyMatch(p -> p.getVenditore().getId().equals(idVenditore));

        if (!isMioOrdine) {
            throw new RuntimeException("Non puoi gestire un ordine che non contiene i tuoi prodotti");
        }

        if ("SPEDITO".equals(ordine.getStato())) {
            throw new RuntimeException("Ordine già spedito");
        }

        for (Prodotto p : ordine.getProdotti()) {
            if (p.getVenditore().getId().equals(idVenditore)) {
                if (p.getQuantitaDisponibile() > 0) {
                    p.setQuantitaDisponibile(p.getQuantitaDisponibile() - 1);
                    prodottoRepo.save(p);
                } else {
                    throw new RuntimeException("Errore: Prodotto '" + p.getNome() + "' esaurito! Impossibile spedire.");
                }
            }
        }

        ordine.setStato("SPEDITO");
        ordineRepo.save(ordine);
    }
}
