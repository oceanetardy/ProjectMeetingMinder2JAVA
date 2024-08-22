package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Reservation;
import com.example.MeetingMinder.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.save(reservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @Valid @RequestBody Reservation reservationDetails) {
        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isPresent()) {
            Reservation updatedReservation = reservation.get();
            updatedReservation.setStartTime(reservationDetails.getStartTime());
            updatedReservation.setEndTime(reservationDetails.getEndTime());
            updatedReservation.setDescription(reservationDetails.getDescription());
            updatedReservation.setUser(reservationDetails.getUser());
            updatedReservation.setRoom(reservationDetails.getRoom());
            return ResponseEntity.ok(reservationService.save(updatedReservation));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable Long id) {
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
