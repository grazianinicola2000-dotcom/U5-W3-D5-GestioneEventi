package nicolagraziani.U5_W3_D5_GestioneEventi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID eventId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "event_date")
    private LocalDate eventDate;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int seats;

    @ManyToOne
    @JoinColumn(nullable = false, name = "creator_id")
    private User creatorId;

    public Event(String title, String description, String location, LocalDate eventDate, int seats, User creatorId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.seats = seats;
        this.creatorId = creatorId;
    }
}
