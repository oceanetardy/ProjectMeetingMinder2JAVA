package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.Reservation;
import com.example.MeetingMinder.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Page<Reservation> findAll(Pageable pageable) {
        logger.info("Obtention de toutes les réservations avec pagination: {}", pageable);
        return reservationRepository.findAll(pageable);
    }

    public Optional<Reservation> findById(Long id) {
        logger.info("Recherche de la réservation avec ID: {}", id);
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        if (isRoomAlreadyReserved(reservation.getRoom().getId(), reservation.getStartTime(), reservation.getEndTime())) {
            logger.warn("Tentative de réservation échouée. La salle avec ID: {} est déjà réservée pour les créneaux horaires de {} à {}",
                    reservation.getRoom().getId(), reservation.getStartTime(), reservation.getEndTime());
            throw new RuntimeException("La salle est déjà réservée pour les créneaux horaires spécifiés.");
        }
        logger.info("Sauvegarde de la réservation pour la salle: {}", reservation.getRoom().getName());
        return reservationRepository.save(reservation);
    }

    public void deleteAll() {
        logger.info("Suppression de toutes les réservations");
        reservationRepository.deleteAll();
    }

    public void deleteById(Long id) {
        logger.info("Suppression de la réservation avec ID: {}", id);
        reservationRepository.deleteById(id);
    }

    private boolean isRoomAlreadyReserved(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                roomId, endTime, startTime);
        return !conflictingReservations.isEmpty();
    }
}
