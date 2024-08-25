package services;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.repository.RoleRepository;
import com.example.MeetingMinder.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Créer des données fictives pour le test
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("User");

        List<Role> roles = Arrays.asList(role1, role2);
        Page<Role> page = new PageImpl<>(roles);
        Pageable pageable = PageRequest.of(0, 2);

        // Configurer le comportement du mock
        when(roleRepository.findAll(pageable)).thenReturn(page);

        // Exécuter la méthode de service
        Page<Role> result = roleService.findAll(pageable);

        // Vérifier le résultat
        assertEquals(2, result.getTotalElements());
        assertEquals("Admin", result.getContent().get(0).getName());
        assertEquals("User", result.getContent().get(1).getName());
    }

    @Test
    void testFindById() {
        // Créer des données fictives pour le test
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        // Configurer le comportement du mock
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Exécuter la méthode de service
        Optional<Role> result = roleService.findById(1L);

        // Vérifier le résultat
        assertTrue(result.isPresent());
        assertEquals("Admin", result.get().getName());
    }

    @Test
    void testSave() {
        // Créer des données fictives pour le test
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        // Configurer le comportement du mock
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Exécuter la méthode de service
        Role result = roleService.save(role);

        // Vérifier le résultat
        assertEquals("Admin", result.getName());
    }

    @Test
    void testSaveWithDuplicateRole() {
        // Créer des données fictives pour le test
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        // Configurer le comportement du mock pour lancer une exception
        when(roleRepository.save(any(Role.class))).thenThrow(new DataIntegrityViolationException("Role already exists"));

        // Exécuter la méthode de service et vérifier l'exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> roleService.save(role));
        assertEquals("Un rôle avec ce nom existe déjà.", thrown.getMessage());
    }

    @Test
    void testDeleteAll() {
        // Exécuter la méthode de service
        roleService.deleteAll();

        // Vérifier l'interaction avec le mock
        verify(roleRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteById() {
        // Exécuter la méthode de service
        roleService.deleteById(1L);

        // Vérifier l'interaction avec le mock
        verify(roleRepository, times(1)).deleteById(1L);
    }
}
