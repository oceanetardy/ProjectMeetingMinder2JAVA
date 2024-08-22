package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Reservation;
import com.example.MeetingMinder.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Obtenir toutes les réservations", description = "Retourne une liste paginée de toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)) })
    @GetMapping
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationService.findAll(pageable);
    }

    @Operation(summary = "Obtenir une réservation par son identifiant", description = "Retourne une réservation en fonction de son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class)) }),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer une nouvelle réservation", description = "Crée une nouvelle réservation et retourne la réservation créée")
    @ApiResponse(responseCode = "201", description = "Réservation créée avec succès",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Reservation.class)) })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        return ResponseEntity.status(201).body(reservationService.save(reservation));
    }

    @Operation(summary = "Mettre à jour une réservation", description = "Met à jour une réservation existante et retourne la réservation mise à jour")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation mise à jour avec succès",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reservation.class)) }),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée",
                    content = @Content)
    })
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

    @Operation(summary = "Supprimer une réservation par son identifiant", description = "Supprime une réservation en fonction de son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable Long id) {
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer toutes les réservations", description = "Supprime toutes les réservations")
    @ApiResponse(responseCode = "204", description = "Toutes les réservations ont été supprimées avec succès")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
