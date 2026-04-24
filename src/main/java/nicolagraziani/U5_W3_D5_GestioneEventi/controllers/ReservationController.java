package nicolagraziani.U5_W3_D5_GestioneEventi.controllers;

import nicolagraziani.U5_W3_D5_GestioneEventi.entities.Reservation;
import nicolagraziani.U5_W3_D5_GestioneEventi.entities.User;
import nicolagraziani.U5_W3_D5_GestioneEventi.exceptions.ValidationException;
import nicolagraziani.U5_W3_D5_GestioneEventi.payloads.ReservationDTO;
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
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENT_CREATOR')")
    public Page<Reservation> findAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(defaultValue = "createdAt") String sortBy) {
        return this.reservationService.findAll(page, size, sortBy);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
//    IN QUESTO CASO HO PREFERITO FARE IN MODO CHE OGNI UTENTE PUÒ PRENOTARE SOLO PER SE STESSO INDIPENDENTEMENTE CHE SIA USER, ORGANIZZATORE O
//    TODO: ADMIN PUÒ PRENOTARE ANCHE PER ALTRI UTENTI
    public Reservation saveReservation(@RequestBody @Validated ReservationDTO body, BindingResult validation, @AuthenticationPrincipal User user) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.reservationService.saveReservation(body, user);
    }

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findReservationByIdAndDelete(@PathVariable UUID reservationId, @AuthenticationPrincipal User user) {
        this.reservationService.findReservationByIdAndDelete(reservationId, user);
    }

}
