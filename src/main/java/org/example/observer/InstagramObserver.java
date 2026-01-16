package org.example.observer;

import org.springframework.stereotype.Component;

@Component
public class InstagramObserver implements SocialObserver {
    @Override
    public void pubblica(String messaggio, String url) {
        System.out.println("[Instagram] Post creato: "+ messaggio+" , Link:  "+url );
    }
}
