package services;

import com.example.MeetingMinder.model.Reservation;
import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.repository.ReservationRepository;
import com.example.MeetingMinder.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Créer des données fictives pour le test
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);

        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setStartTime(LocalDateTime.now().plusDays(1));
        reservation1.setEndTime(LocalDateTime.now().plusDays(2));
        reservation1.setUser(user);
        reservation1.setRoom(room);

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setStartTime(LocalDateTime.now().plusDays(3));
        reservation2.setEndTime(LocalDateTime.now().plusDays(4));
        reservation2.setUser(user);
        reservation2.setRoom(room);

        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        Page<Reservation> page = new PageImpl<>(reservations);
        Pageable pageable = PageRequest.of(0, 2);

        // Configurer le comportement du mock
        when(reservationRepository.findAll(pageable)).thenReturn(page);

        // Exécuter la méthode de service
        Page<Reservation> result = reservationService.findAll(pageable);

        // Vérifier le résultat
        assertEquals(2, result.getTotalElements());
        assertEquals(reservation1.getId(), result.getContent().get(0).getId());
        assertEquals(reservation2.getId(), result.getContent().get(1).getId());
    }

    @Test
    void testFindById() {
        // Créer des données fictives pour le test
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusDays(2));
        reservation.setUser(user);
        reservation.setRoom(room);

        // Configurer le comportement du mock
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Exécuter la méthode de service
        Optional<Reservation> result = reservationService.findById(1L);

        // Vérifier le résultat
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testSave() {
        // Créer des données fictives pour le test
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusDays(2));
        reservation.setUser(user);
        reservation.setRoom(room);

        // Configurer le comportement du mock
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Exécuter la méthode de service
        Reservation result = reservationService.save(reservation);

        // Vérifier le résultat
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeleteAll() {
        // Exécuter la méthode de service
        reservationService.deleteAll();

        // Vérifier l'interaction avec le mock
        verify(reservationRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteById() {
        // Exécuter la méthode de service
        reservationService.deleteById(1L);

        // Vérifier l'interaction avec le mock
        verify(reservationRepository, times(1)).deleteById(1L);
    }
}
