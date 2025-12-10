package org.example.controller;
import org.example.model.Utente;
import org.example.model.RuoloUtente;
import org.example.factory.UtenteFactory;
import org.example.repository.UtenteRepository;
import java.util.Map;
public class  AuthController {
    private UtenteRepository utenteRepo;
    private UtenteFactory utenteFactory;

    // Dependency Injection (passiamo le istanze nel costruttore)
    public AuthController(UtenteRepository utenteRepo) {
        this.utenteRepo = utenteRepo;
        this.utenteFactory = new UtenteFactory();
    }

    public boolean registrazione(Map<String, Object> dati) {
        RuoloUtente ruolo = RuoloUtente.valueOf((String) dati.get("ruolo")); // Conversione stringa -> Enum
        Utente nuovoUtente = utenteFactory.creaUtente(ruolo, dati);

        // CORREZIONE: usa save() invece di salva()
        utenteRepo.save(nuovoUtente);
        return true;
    }

    public Utente login(String email, String password) {
        // CORREZIONE: usa findByEmail() che restituisce Optional
        Utente u = utenteRepo.findByEmail(email).orElse(null);

        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
}
