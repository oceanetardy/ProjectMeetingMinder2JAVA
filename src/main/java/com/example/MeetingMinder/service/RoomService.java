package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<Room> findAll(Pageable pageable) {
        logger.info("Obtention de toutes les salles avec pagination: {}", pageable);
        return roomRepository.findAll(pageable);
    }

    public Optional<Room> findById(Long id) {
        logger.info("Recherche de la salle avec ID: {}", id);
        return roomRepository.findById(id);
    }

    public Room save(Room room) {
        logger.info("Sauvegarde de la salle: {}", room.getName());
        return roomRepository.save(room);
    }

    public void deleteAll() {
        logger.info("Suppression de toutes les salles");
        roomRepository.deleteAll();
    }

    public void deleteById(Long id) {
        logger.info("Suppression de la salle avec ID: {}", id);
        roomRepository.deleteById(id);
    }
}
