package nicolagraziani.U5_W3_D5_GestioneEventi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank(message = "L'email è un campo obbligatorio e non può essere una Stringa vuota")
                       @Email(message = "L'email inserita non è nel formato corretto")
                       String email,
                       @NotBlank(message = "La password è obbligatoria")
                       String password) {
}
