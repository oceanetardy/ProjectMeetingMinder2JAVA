package services;

import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.repository.UserRepository;
import com.example.MeetingMinder.service.UserService;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Créer des données fictives pour le test
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setPassword("password456");

        List<User> users = Arrays.asList(user1, user2);
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 2);

        // Configurer le comportement du mock
        when(userRepository.findAll(pageable)).thenReturn(page);

        // Exécuter la méthode de service
        Page<User> result = userService.findAll(pageable);

        // Vérifier le résultat
        assertEquals(2, result.getTotalElements());
        assertEquals(user1.getName(), result.getContent().get(0).getName());
        assertEquals(user2.getName(), result.getContent().get(1).getName());
    }

    @Test
    public void testFindById() {
        // Créer des données fictives pour le test
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setPassword("password123");

        // Configurer le comportement du mock
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Exécuter la méthode de service
        Optional<User> result = userService.findById(1L);

        // Vérifier le résultat
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    public void testSave() {
        // Créer des données fictives pour le test
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setPassword("password123");

        // Configurer le comportement du mock
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Exécuter la méthode de service
        User result = userService.save(user);

        // Vérifier le résultat
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testDeleteAll() {
        // Exécuter la méthode de service
        userService.deleteAll();

        // Vérifier l'interaction avec le mock
        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteById() {
        // Exécuter la méthode de service
        userService.deleteById(1L);

        // Vérifier l'interaction avec le mock
        verify(userRepository, times(1)).deleteById(1L);
    }
}
