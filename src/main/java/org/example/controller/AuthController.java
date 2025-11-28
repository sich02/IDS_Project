package org.example.controller;
import org.example.model.Utente;
import org.example.model.RuoloUtente;
import org.example.factory.UtenteFactory;
import org.example.repository.UtenteRepository;
import java.util.Map;
public class AuthController {
    private UtenteRepository utenteRepo;
    private UtenteFactory utenteFactory;

    // Dependency Injection (passiamo le istanze nel costruttore)
    public AuthController(UtenteRepository utenteRepo) {
        this.utenteRepo = utenteRepo;
        this.utenteFactory = new UtenteFactory();
    }

    public boolean registrazione(Map<String, Object> dati) {
        RuoloUtente ruolo = (RuoloUtente) dati.get("ruolo");
        // 1. Creazione tramite Factory
        Utente nuovoUtente = utenteFactory.creaUtente(ruolo, dati);
        // 2. Salvataggio (su interfaccia)
        utenteRepo.salva(nuovoUtente);
        return true;
    }

    public Utente login(String email, String password) {
        Utente u = utenteRepo.trovaPerEmail(email);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null; // O lancia eccezione
    }
}
