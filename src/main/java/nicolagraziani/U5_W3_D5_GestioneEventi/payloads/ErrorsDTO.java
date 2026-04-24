package nicolagraziani.U5_W3_D5_GestioneEventi.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
