package nicolagraziani.U5_W3_D5_GestioneEventi.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EventUpdateDTO(
        @NotBlank(message = "Il titolo è un campo obbligatorio e non può essere una Stringa vuota")
        @Size(min = 2, max = 50, message = "Il titolo deve essere compreso tra 2 e 50 caratteri")
        String title,
        @NotBlank(message = "La descrizione è un campo obbligatorio e non può essere una Stringa vuota")
        @Size(min = 2, message = "La descrizione deve avere almeno 2 caratteri")
        String description,
        @NotNull(message = "La data dell'evento è un campo obbligatorio e non può essere vuota")
        LocalDate eventDate,
        @NotBlank(message = "La location è un campo obbligatorio e non può essere una Stringa vuota")
        String location,
        @NotNull(message = "Il numero di posti è obbligatorio")
        int seats
) {
}
