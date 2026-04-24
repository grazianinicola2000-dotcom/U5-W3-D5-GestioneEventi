package nicolagraziani.U5_W3_D5_GestioneEventi.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReservationDTO(
        @NotNull(message = "L'id dell'evento è obbligatorio")
        UUID eventId
) {
}
