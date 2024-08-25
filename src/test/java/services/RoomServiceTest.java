package services;

import com.example.MeetingMinder.model.Room;
import com.example.MeetingMinder.repository.RoomRepository;
import com.example.MeetingMinder.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Créer des données fictives pour le test
        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Room A");
        room1.setCapacity(10);
        room1.setDescription("Description A");

        Room room2 = new Room();
        room2.setId(2L);
        room2.setName("Room B");
        room2.setCapacity(20);
        room2.setDescription("Description B");

        List<Room> rooms = Arrays.asList(room1, room2);
        Page<Room> page = new PageImpl<>(rooms);
        Pageable pageable = PageRequest.of(0, 2);

        // Configurer le comportement du mock
        when(roomRepository.findAll(pageable)).thenReturn(page);

        // Exécuter la méthode de service
        Page<Room> result = roomService.findAll(pageable);

        // Vérifier le résultat
        assertEquals(2, result.getTotalElements());
        assertEquals("Room A", result.getContent().get(0).getName());
        assertEquals(10, result.getContent().get(0).getCapacity());
        assertEquals("Description A", result.getContent().get(0).getDescription());
        assertEquals("Room B", result.getContent().get(1).getName());
        assertEquals(20, result.getContent().get(1).getCapacity());
        assertEquals("Description B", result.getContent().get(1).getDescription());
    }

    @Test
    void testFindById() {
        // Créer des données fictives pour le test
        Room room = new Room();
        room.setId(1L);
        room.setName("Room A");
        room.setCapacity(10);
        room.setDescription("Description A");

        // Configurer le comportement du mock
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Exécuter la méthode de service
        Optional<Room> result = roomService.findById(1L);

        // Vérifier le résultat
        assertTrue(result.isPresent());
        assertEquals("Room A", result.get().getName());
        assertEquals(10, result.get().getCapacity());
        assertEquals("Description A", result.get().getDescription());
    }

    @Test
    void testSave() {
        // Créer des données fictives pour le test
        Room room = new Room();
        room.setId(1L);
        room.setName("Room A");
        room.setCapacity(10);
        room.setDescription("Description A");

        // Configurer le comportement du mock
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Exécuter la méthode de service
        Room result = roomService.save(room);

        // Vérifier le résultat
        assertEquals("Room A", result.getName());
        assertEquals(10, result.getCapacity());
        assertEquals("Description A", result.getDescription());
    }

    @Test
    void testDeleteAll() {
        // Exécuter la méthode de service
        roomService.deleteAll();

        // Vérifier l'interaction avec le mock
        verify(roomRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteById() {
        // Exécuter la méthode de service
        roomService.deleteById(1L);

        // Vérifier l'interaction avec le mock
        verify(roomRepository, times(1)).deleteById(1L);
    }
}
