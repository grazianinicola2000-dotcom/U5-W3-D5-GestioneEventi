package nicolagraziani.U5_W3_D5_GestioneEventi.services;

import lombok.extern.slf4j.Slf4j;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Event;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Reservation;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.enums.Role;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.NotFoundException;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.UnauthorizedException;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.ValidationException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.ReservationDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.repositories.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final EventService eventService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, EventService eventService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    public Reservation saveReservation(ReservationDTO body, User user) {
        Event event = this.eventService.findEventById(body.eventId());
//        CONTROLLO PER VERIFICARE CHE UN UTENTE NON ABBIA VIAGGI IN PROGRAMMA LO STESSO GIORNO
        if (this.reservationRepository.existsByUserAndEvent_EventDate(user, event.getEventDate())) {
            throw new ValidationException("Impossibile prenotare l'evento, l'utente " + user.getSurname() + " ha gia un evento prenotato per la data " + event.getEventDate());
        }
        if (event.getSeats() < 1) {
            throw new ValidationException("Impossibile effettuare la prenotazione, i posti sono esauriti");
        }
        event.setSeats(event.getSeats() - 1);
        Reservation newReservation = new Reservation(event, user);
        this.reservationRepository.save(newReservation);
        log.info("La prenotazione è stata effetutata con successo");
        return newReservation;
    }

    public Reservation findReservationById(UUID reservationId) {
        return this.reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException(reservationId));
    }

    public Page<Reservation> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.reservationRepository.findAll(pageable);
    }

    public void findReservationByIdAndDelete(UUID reservationId, User user) {
        Reservation found = this.findReservationById(reservationId);
        if (user.getRole() != Role.ADMIN && !found.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException("Solo l'utente che ha effettuato la prenotazione o un admin possono cancellare questa prenotazione");
        }
        Event event = found.getEvent();
        event.setSeats(event.getSeats() + 1);
        this.reservationRepository.delete(found);
        log.info("La prenotazioen con id {} è stata eliminata con successo", found.getReservationId());
    }

    public List<Event> findEventListByUserReservation(User user) {
        List<Reservation> reservations = reservationRepository.findByUser(user);

        List<Event> events = reservations.stream().map(Reservation::getEvent).toList();
        if (events.isEmpty()) {
            throw new NotFoundException("Al momento non hai prenotazioni");
        }
        return events;
    }
}
