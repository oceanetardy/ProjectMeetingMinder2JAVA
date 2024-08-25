package com.example.MeetingMinder.repository;

import com.example.MeetingMinder.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long roomId, LocalDateTime endTime, LocalDateTime startTime);

}
