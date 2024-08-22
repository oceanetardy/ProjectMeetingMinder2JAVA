package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Obtenir toutes les salles", description = "Retourne une liste paginée de toutes les salles")
    @ApiResponse(responseCode = "200", description = "Liste des salles récupérée avec succès",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)) })
    @GetMapping
    public Page<Room> getAllRooms(Pageable pageable) {
        return roomService.findAll(pageable);
    }

    @Operation(summary = "Obtenir une salle par son identifiant", description = "Retourne une salle en fonction de son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salle trouvée",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.findById(id);
        return room.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer une nouvelle salle", description = "Crée une nouvelle salle et retourne la salle créée")
    @ApiResponse(responseCode = "201", description = "Salle créée avec succès",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Room.class)) })
    @PostMapping
    public Room createRoom(@Valid @RequestBody Room room) {
        return roomService.save(room);
    }

    @Operation(summary = "Mettre à jour une salle", description = "Met à jour une salle existante et retourne la salle mise à jour")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salle mise à jour avec succès",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomDetails) {
        Optional<Room> room = roomService.findById(id);
        if (room.isPresent()) {
            Room updatedRoom = room.get();
            updatedRoom.setName(roomDetails.getName());
            updatedRoom.setCapacity(roomDetails.getCapacity());
            updatedRoom.setDescription(roomDetails.getDescription());
            return ResponseEntity.ok(roomService.save(updatedRoom));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer une salle par son identifiant", description = "Supprime une salle en fonction de son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Salle supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id) {
        if (roomService.findById(id).isPresent()) {
            roomService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer toutes les salles", description = "Supprime toutes les salles")
    @ApiResponse(responseCode = "204", description = "Toutes les salles ont été supprimées avec succès")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllRooms() {
        roomService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
