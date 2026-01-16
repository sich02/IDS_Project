package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.model.Contenuto;
import org.example.observer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class SocialService {
    private final List<SocialObserver> observers = new ArrayList<>();

    @Autowired private FacebookObserver facebookObserver;
    @Autowired private InstagramObserver instagramObserver;

    @PostConstruct
    public void init(){
        registraObserver(facebookObserver);
        registraObserver(instagramObserver);
    }

    private void registraObserver(SocialObserver observer) {
        observers.add(observer);
    }

    private void rimuoviObserver(SocialObserver observer) {
        observers.remove(observer);
    }

    public void condividiContenuto(Contenuto contenuto){
        String messaggio = "Nuovo contenuto imperdibile: " + contenuto.getNome();
        String url = "https://www.filiera-agricola.it/contenuti/"+contenuto.getId();

        System.out.println(">>> Avvio condivisione sui social per: " + contenuto.getNome());

        for (SocialObserver observer : observers){
            observer.pubblica(messaggio, url);
        }
    }
}
