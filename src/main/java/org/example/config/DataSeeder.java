package org.example.config;


import org.example.model.Gestore;
import org.example.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UtenteRepository utenteRepo;

    @Override
    public void run(String... args) throws Exception {
        if(!utenteRepo.existsByEmail("admin@farm.it")){
            Gestore admin = new Gestore(
                    "Super",
                    "Admin",
                    "admin@farm.it",
                    "admin"
            );

            admin.setAccreditato(true);

            utenteRepo.save(admin);

            System.out.println(">>> ADMIN CREATO");
        }
    }

}
