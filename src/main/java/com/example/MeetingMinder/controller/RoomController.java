package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Gestion des salles de réunion", description = "Opérations liées à la gestion des salles de réunion dans l'application")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Obtenir une salle par ID",
            description = "Retourne une salle de réunion en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salle trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(
            @Parameter(description = "ID de la salle à récupérer", example = "1") @PathVariable Long id) {
        logger.info("Requête pour obtenir la salle avec ID: {}", id);
        Optional<Room> room = roomService.findById(id);
        if (room.isPresent()) {
            logger.info("Salle trouvée: {}", room.get().getName());
            return ResponseEntity.ok(room.get());
        } else {
            logger.warn("Salle avec ID: {} non trouvée", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtenir toutes les salles",
            description = "Retourne une liste paginée de toutes les salles de réunion")
    @ApiResponse(responseCode = "200", description = "Liste des salles récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping
    public Page<Room> getAllRooms(Pageable pageable) {
        logger.info("Requête pour obtenir toutes les salles avec pagination: {}", pageable);
        return roomService.findAll(pageable);
    }

    @Operation(summary = "Créer une nouvelle salle de réunion",
            description = "Crée une nouvelle salle de réunion et retourne la salle créée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Salle créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Room> createRoom(
            @Parameter(description = "Détails de la salle à créer", required = true)
            @Valid @RequestBody Room room) {
        logger.info("Requête pour créer une nouvelle salle: {}", room.getName());
        try {
            Room createdRoom = roomService.save(room);
            logger.info("Salle créée avec succès: {}", createdRoom.getName());
            return ResponseEntity.status(201).body(createdRoom);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la salle : {}", room.getName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Mettre à jour une salle de réunion par ID",
            description = "Met à jour une salle de réunion existante avec les nouvelles informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salle mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @Parameter(description = "ID de la salle à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Nouvelles informations de la salle", required = true)
            @Valid @RequestBody Room roomDetails) {
        logger.info("Requête pour mettre à jour la salle avec ID: {}", id);
        Optional<Room> room = roomService.findById(id);
        if (room.isPresent()) {
            Room updatedRoom = room.get();
            updatedRoom.setName(roomDetails.getName());
            updatedRoom.setCapacity(roomDetails.getCapacity());
            updatedRoom.setDescription(roomDetails.getDescription());
            Room savedRoom = roomService.save(updatedRoom);
            logger.info("Salle mise à jour avec succès: {}", savedRoom.getName());
            return ResponseEntity.ok(savedRoom);
        } else {
            logger.warn("Salle avec ID: {} non trouvée pour mise à jour", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour partiellement une salle par ID",
            description = "Met à jour partiellement une salle existante avec les informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salle mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Room> partialUpdateRoom(
            @Parameter(description = "ID de la salle à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour", required = true)
            @RequestBody Map<String, Object> updates) {
        logger.info("Requête pour mise à jour partielle de la salle avec ID: {}", id);
        Optional<Room> roomOptional = roomService.findById(id);

        if (!roomOptional.isPresent()) {
            logger.warn("Salle avec ID: {} non trouvée pour mise à jour partielle", id);
            return ResponseEntity.notFound().build();
        }

        Room room = roomOptional.get();

        updates.forEach((key, value) -> {
            logger.info("Mise à jour du champ: {} avec la valeur: {}", key, value);
            switch (key) {
                case "name":
                    room.setName((String) value);
                    break;
                case "capacity":
                    room.setCapacity((Integer) value);
                    break;
                case "description":
                    room.setDescription((String) value);
                    break;
            }
        });

        Room updatedRoom = roomService.save(room);
        logger.info("Mise à jour partielle réussie pour la salle: {}", updatedRoom.getName());
        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(summary = "Supprimer une salle par ID",
            description = "Supprime une salle en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Salle supprimée avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(
            @Parameter(description = "ID de la salle à supprimer", example = "1") @PathVariable Long id) {
        logger.info("Requête pour supprimer la salle avec ID: {}", id);
        if (roomService.findById(id).isPresent()) {
            roomService.deleteById(id);
            logger.info("Salle avec ID: {} supprimée avec succès", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Salle avec ID: {} non trouvée pour suppression", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer toutes les salles",
            description = "Supprime toutes les salles existantes dans le système")
    @ApiResponse(responseCode = "204", description = "Toutes les salles ont été supprimées avec succès", content = @Content)
    @DeleteMapping
    public ResponseEntity<Void> deleteAllRooms() {
        logger.info("Requête pour supprimer toutes les salles");
        roomService.deleteAll();
        logger.info("Toutes les salles ont été supprimées avec succès");
        return ResponseEntity.noContent().build();
    }
}
