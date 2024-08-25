package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.repository.RoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Cherche un rôle par son ID
    public Optional<Role> findById(Long id) {
        logger.info("Recherche du rôle avec ID: {}", id);
        return roleRepository.findById(id);
    }

    // Récupère tous les rôles avec pagination
    public Page<Role> findAll(Pageable pageable) {
        logger.info("Obtention de tous les rôles avec pagination: {}", pageable);
        return roleRepository.findAll(pageable);
    }

    // Sauvegarde un rôle dans la base de données
    public Role save(Role role) {
        try {
            logger.info("Sauvegarde du rôle: {}", role.getName());
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erreur lors de la sauvegarde du rôle: {}", role.getName(), e);
            throw new RuntimeException("Un rôle avec ce nom existe déjà.");
        }
    }

    // Supprime un rôle par son ID
    public void deleteById(Long id) {
        logger.info("Suppression du rôle avec ID: {}", id);
        roleRepository.deleteById(id);
    }

    // Supprime tous les rôles
    public void deleteAll() {
        logger.info("Suppression de tous les rôles");
        roleRepository.deleteAll();
    }
}
