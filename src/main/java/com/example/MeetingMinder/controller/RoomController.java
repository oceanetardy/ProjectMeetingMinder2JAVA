package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public Page<Room> getAllRooms(Pageable pageable) {
        return roomService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.findById(id);
        return room.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Room createRoom(@Valid @RequestBody Room room) {
        return roomService.save(room);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id) {
        if (roomService.findById(id).isPresent()) {
            roomService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllRooms() {
        roomService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
