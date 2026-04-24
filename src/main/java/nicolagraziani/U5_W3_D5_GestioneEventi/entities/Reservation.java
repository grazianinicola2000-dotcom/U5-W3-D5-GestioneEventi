package nicolagraziani.U5_W3_D5_GestioneEventi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Reservation {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID reservationId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public Reservation(Event event, User user) {
        this.event = event;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
