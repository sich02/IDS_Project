package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcquirenteService {

    @Autowired private UtenteRepository utenteRepo;
    @Autowired private ProdottoRepository prodottoRepo;
    @Autowired private CarrelloRepository carrelloRepo;
    @Autowired private OrdineRepository ordineRepo;
    @Autowired private EventoRepository eventoRepo;
    @Autowired private PrenotazioneRepository prenotazioneRepo;


    //viualizza la lista degli eventi
    public List<Evento> getEventiDisponibili(){
        return eventoRepo.findAll().stream()
                .filter(e ->"PUBBLICATO".equals(e.getStatoNome()))
                .toList();
    }

    //visualizza carrello, se vuoto ritorna un carrello vuoto

    public Carrello getCarrello(Long idAcquirente) {
        Acquirente acquirente = (Acquirente) utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        return carrelloRepo.findByAcquirente(acquirente)
                .orElse(new Carrello(acquirente));
    }


    //aggiungi al carrello
    @Transactional
    public void aggiungiAlCarrello(Long idAcquirente, Long idProdotto) {
        Acquirente acquirente = (Acquirente) utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Prodotto prodotto = prodottoRepo.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if (!"PUBBLICATO".equals(prodotto.getStatoNome())) {
            throw new RuntimeException("Prodotto non disponibile per l'acquisto.");
        }

        Carrello carrello = carrelloRepo.findByAcquirente(acquirente)
                .orElse(new Carrello(acquirente));

        carrello.getProdotti().add(prodotto);
        carrelloRepo.save(carrello);
    }


    //rimuovi dal carrello
    @Transactional
    public void rimuoviDalCarrello(Long idAcquirente, Long idProdotto) {
        Acquirente acquirente = (Acquirente)  utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Carrello carrello = (Carrello) carrelloRepo.findByAcquirente(acquirente)
                .orElseThrow(() -> new RuntimeException("Carrello vuoto o non trovato"));

        boolean rimosso = carrello.getProdotti().removeIf(p -> p.getId().equals(idProdotto));

        if(!rimosso){
            throw new RuntimeException("Prodotto non trovato nel carrello");
        }

        carrelloRepo.save(carrello);
    }


    //effettua ordine
    @Transactional
    public Ordine effettuaOrdine(Long idAcquirente) {
        Acquirente acquirente = (Acquirente) utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Carrello carrello = carrelloRepo.findByAcquirente(acquirente)
                .orElseThrow(() -> new RuntimeException("Carrello vuoto"));

        if (carrello.getProdotti().isEmpty()) throw new RuntimeException("Carrello vuoto");

        double totale = carrello.getProdotti().stream().mapToDouble(Prodotto::getPrezzo).sum();

        // Crea Ordine
        Ordine ordine = new Ordine(acquirente, new ArrayList<>(carrello.getProdotti()), totale);
        ordineRepo.save(ordine);

        carrello.getProdotti().clear();
        carrelloRepo.save(carrello);

        return ordine;
    }

    //annulla l'ordine
    @Transactional
    public void annullaOrdine(Long idOrdine, Long idAcquirente) {
        Ordine ordine = ordineRepo.findById(idOrdine)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));
        if(!"CREATO".equals(ordine.getStato())){
            throw new RuntimeException("Impossibile annullare l'ordine. Stato attuale: "+ordine.getStato());
        }

        ordine.setStato("ANNULLATO");
        ordineRepo.save(ordine);
    }

    //prenota evento
    @Transactional
    public void prenotaEvento(Long idAcquirente, Long idEvento) {
        Acquirente acquirente = (Acquirente) utenteRepo.findById(idAcquirente)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Evento evento = eventoRepo.findById(idEvento)
                .orElseThrow(()->new RuntimeException("Evento non trovato"));

        if(!"PUBBLICATO".equals(evento.getStatoNome())){
            throw new RuntimeException("Evento non disponibile");
        }

        Prenotazione prenotazione = new Prenotazione(acquirente, evento);
        prenotazioneRepo.save(prenotazione);
    }

    //annulla la prenotazione
    @Transactional
    public void annullaPrenotazione(Long idPrenotazione, Long idAcquirente) {
        Prenotazione prenotazione = prenotazioneRepo.findById(idPrenotazione)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        if(!prenotazione.getAcquirente().getId().equals(idAcquirente)){
            throw new RuntimeException("Non puoi annullare una prenotazione non tua");
        }
        prenotazioneRepo.delete(prenotazione);
    }
}