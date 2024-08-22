package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<Room> findAll(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteAll() {
        roomRepository.deleteAll();
    }

    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
}
