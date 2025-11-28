package org.example.repository;

import  org.example.model.Utente;

public interface UtenteRepository {
    void salva(Utente utente);
    Utente trovaPerEmail(String email);
}
