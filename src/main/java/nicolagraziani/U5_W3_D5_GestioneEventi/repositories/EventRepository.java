package nicolagraziani.U5_W3_D5_GestioneEventi.repositories;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
}
