package nicolagraziani.U5_W3_D5_GestioneEventi.repositories;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Reservation;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    boolean existsByUserAndEvent_EventDate(User user, LocalDate eventDate);

    List<Reservation> findByUser(User user);
}
