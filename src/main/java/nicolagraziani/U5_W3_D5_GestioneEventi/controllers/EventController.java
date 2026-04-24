package nicolagraziani.U5_W3_D5_GestioneEventi.controllers;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Event;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.ValidationException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.EventDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.EventUpdateDTO;
import nicolagraziani.U5_W3_D5_GestioneEventi.services.EventService;
import nicolagraziani.U5_W3_D5_GestioneEventi.services.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final ReservationService reservationService;

    public EventController(EventService eventService, ReservationService reservationService) {
        this.eventService = eventService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public Page<Event> findAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "eventDate") String sortBy) {
        return this.eventService.findAll(page, size, sortBy);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENT_CREATOR')")
    public Event saveEvent(@RequestBody @Validated EventDTO body, BindingResult validation, @AuthenticationPrincipal User user) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.eventService.saveEvent(body, user);
    }

    @GetMapping("/{eventId}")
    public Event findById(@PathVariable UUID eventId) {
        return this.eventService.findEventById(eventId);
    }


    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENT_CREATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findEventByIdAndDelete(@PathVariable UUID eventId, @AuthenticationPrincipal User user) {
        this.eventService.findEventByIdAndDelete(eventId, user);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENT_CREATOR')")
    public Event findEventByIdAndUpdate(@PathVariable UUID eventId, @AuthenticationPrincipal User user, @RequestBody @Validated EventUpdateDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.eventService.findEventByIdAndUpdate(eventId, user, body);
    }

    @GetMapping("/myEvents")
    public List<Event> getMyEvents(@AuthenticationPrincipal User user) {
        return this.reservationService.findEventListByUserReservation(user);
    }

}
