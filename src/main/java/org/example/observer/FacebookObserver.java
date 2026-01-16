package org.example.observer;

import org.springframework.stereotype.Component;

@Component
public class FacebookObserver implements SocialObserver {
        @Override
        public void pubblica(String messaggio, String url) {
            System.out.println("[Facebook] Post creato: "+messaggio+" , Link:  "+url );
        }
}
