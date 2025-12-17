package org.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.model.RuoloUtente;
public record RegistrazioneRequest(
        @NotBlank(message = "Campo obbligatorio")
        String nome,
        @NotBlank(message = "Campo obbligatorio")
        String cognome,
        @NotBlank(message = "Campo obbligatorio")
        @Email(message = "Formato mail non conforme")
        String email,
        @NotBlank(message = "Campo obbligatorio")
        @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        String password,
        @NotBlank(message = "Campo obbligatorio")
        RuoloUtente ruolo,

        String partitaIva,
        String ragioneSociale,
        String indirizzoSede,

        String indirizzoSpedizione
) {
}
