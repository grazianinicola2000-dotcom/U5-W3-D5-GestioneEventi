package nicolagraziani.U5_W3_D5_GestioneEventi.services;

import lombok.extern.slf4j.Slf4j;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Event;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.enums.Role;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.NotFoundException;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.UnauthorizedException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.EventDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.EventUpdateDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.repositories.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    private final UserService userService;

    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public Page<Event> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventRepository.findAll(pageable);
    }

    public Event saveEvent(EventDTO body) {
        User found = this.userService.findUserById(body.creatorId());
        Event event = new Event(body.title(), body.description(), body.location(), body.eventDate(), body.seats(), found);
        this.eventRepository.save(event);
        log.info("L'evento {} nella data {} è stato salvato correttamente", body.title(), body.eventDate());
        return event;
    }

    public Event findEventById(UUID eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
    }

    public void findEventByIdAndDelete(UUID eventId, User user) {
        Event found = this.findEventById(eventId);
        if (user.getRole() != Role.ADMIN && !found.getCreatorId().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException("Solo il creatore dell'evento o un admin possono eliminare questo evento");
        }
        this.eventRepository.delete(found);
        log.info("L'evento {} nella data {} è stato eliminato correttamente", found.getTitle(), found.getEventDate());
    }

    public Event findEventByIdAndUpdate(UUID eventId, User user, EventUpdateDTO body) {
        Event found = this.findEventById(eventId);
        if (user.getRole() != Role.ADMIN && !found.getCreatorId().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException("Solo il creatore dell'evento o un admin possono modificare questo evento");
        }
        found.setEventDate(body.eventDate());
        found.setTitle(body.title());
        found.setDescription(body.description());
        found.setLocation(body.location());
        found.setSeats(body.seats());
        Event saved = this.eventRepository.save(found);
        log.info("L'evento {} nella data {} è stato modificato correttamente", found.getTitle(), found.getEventDate());
        return saved;
    }
}
