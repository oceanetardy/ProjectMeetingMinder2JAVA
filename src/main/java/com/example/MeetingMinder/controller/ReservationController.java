package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Reservation;
import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.service.ReservationService;
import com.example.MeetingMinder.service.RoomService;
import com.example.MeetingMinder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Gestion des réservations", description = "Opérations liées à la gestion des réservations dans l'application")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final RoomService roomService;
    private final UserService userService;

    @Autowired
    public ReservationController(ReservationService reservationService, RoomService roomService, UserService userService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @Operation(summary = "Obtenir une réservation par ID", description = "Retourne une réservation en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @Parameter(description = "ID de la réservation à récupérer", example = "1") @PathVariable Long id) {
        logger.info("Requête pour obtenir la réservation avec ID: {}", id);
        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isPresent()) {
            logger.info("Réservation trouvée pour l'ID: {}", id);
            return ResponseEntity.ok(reservation.get());
        } else {
            logger.warn("Réservation avec ID: {} non trouvée", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtenir toutes les réservations", description = "Retourne une liste paginée de toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping
    public Page<Reservation> getAllReservations(Pageable pageable) {
        logger.info("Requête pour obtenir toutes les réservations avec pagination: {}", pageable);
        return reservationService.findAll(pageable);
    }

    @Operation(summary = "Créer une nouvelle réservation", description = "Crée une nouvelle réservation et retourne la réservation créée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @Parameter(description = "Détails de la réservation à créer", required = true)
            @Valid @RequestBody Reservation reservation) {
        logger.info("Requête pour créer une nouvelle réservation pour la salle: {}", reservation.getRoom().getName());
        Reservation createdReservation = reservationService.save(reservation);
        logger.info("Réservation créée avec succès pour la salle: {}", createdReservation.getRoom().getName());
        return ResponseEntity.status(201).body(createdReservation);
    }

    @Operation(summary = "Mettre à jour une réservation par ID", description = "Met à jour une réservation existante avec les nouvelles informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @Parameter(description = "ID de la réservation à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Nouvelles informations de la réservation", required = true)
            @Valid @RequestBody Reservation reservationDetails) {
        logger.info("Requête pour mettre à jour la réservation avec ID: {}", id);
        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isPresent()) {
            Reservation updatedReservation = reservation.get();
            updatedReservation.setStartTime(reservationDetails.getStartTime());
            updatedReservation.setEndTime(reservationDetails.getEndTime());
            updatedReservation.setDescription(reservationDetails.getDescription());
            updatedReservation.setUser(reservationDetails.getUser());
            updatedReservation.setRoom(reservationDetails.getRoom());
            Reservation savedReservation = reservationService.save(updatedReservation);
            logger.info("Réservation mise à jour avec succès pour l'ID: {}", savedReservation.getId());
            return ResponseEntity.ok(savedReservation);
        } else {
            logger.warn("Réservation avec ID: {} non trouvée pour mise à jour", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour partiellement une réservation par ID", description = "Met à jour partiellement une réservation existante avec les informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Reservation> partialUpdateReservation(
            @Parameter(description = "ID de la réservation à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour", required = true)
            @RequestBody Map<String, Object> updates) {
        logger.info("Requête pour mise à jour partielle de la réservation avec ID: {}", id);
        Optional<Reservation> reservationOptional = reservationService.findById(id);

        if (reservationOptional.isEmpty()) {
            logger.warn("Réservation avec ID: {} non trouvée pour mise à jour partielle", id);
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = reservationOptional.get();

        updates.forEach((key, value) -> {
            logger.info("Mise à jour du champ: {} avec la valeur: {}", key, value);
            switch (key) {
                case "startTime":
                    reservation.setStartTime(LocalDateTime.parse((String) value));
                    break;
                case "endTime":
                    reservation.setEndTime(LocalDateTime.parse((String) value));
                    break;
                case "description":
                    reservation.setDescription((String) value);
                    break;
                case "user":
                    if (value instanceof Map<?, ?> userMap) {
                        Object userIdObj = userMap.get("id");
                        if (userIdObj instanceof Number) {
                            Long userId = ((Number) userIdObj).longValue();
                            User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                            reservation.setUser(user);
                        }
                    }
                    break;
                case "room":
                    if (value instanceof Map<?, ?> roomMap) {
                        Object roomIdObj = roomMap.get("id");
                        if (roomIdObj instanceof Number) {
                            Long roomId = ((Number) roomIdObj).longValue();
                            Room room = roomService.findById(roomId).orElseThrow(() -> new RuntimeException("Salle non trouvée"));
                            reservation.setRoom(room);
                        }
                    }
                    break;
            }
        });

        Reservation updatedReservation = reservationService.save(reservation);
        logger.info("Mise à jour partielle réussie pour la réservation avec ID: {}", updatedReservation.getId());
        return ResponseEntity.ok(updatedReservation);
    }


    @Operation(summary = "Supprimer une réservation par ID", description = "Supprime une réservation en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationById(
            @Parameter(description = "ID de la réservation à supprimer", example = "1") @PathVariable Long id) {
        logger.info("Requête pour supprimer la réservation avec ID: {}", id);
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            logger.info("Réservation avec ID: {} supprimée avec succès", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Réservation avec ID: {} non trouvée pour suppression", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer toutes les réservations", description = "Supprime toutes les réservations existantes dans le système")
    @ApiResponse(responseCode = "204", description = "Toutes les réservations ont été supprimées avec succès", content = @Content)
    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        logger.info("Requête pour supprimer toutes les réservations");
        reservationService.deleteAll();
        logger.info("Toutes les réservations ont été supprimées avec succès");
        return ResponseEntity.noContent().build();
    }
}
