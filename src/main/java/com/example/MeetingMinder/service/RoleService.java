package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.repository.RoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Role save(Role role) {
        try {
            logger.info("Saving role: {}", role.getName());
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error while saving role: {}", role.getName(), e);
            throw new RuntimeException("Un rôle avec ce nom existe déjà.");
        }
    }

    public void deleteAll() {
        logger.info("Deleting all roles");
        roleRepository.deleteAll();
    }

    public void deleteById(Long id) {
        logger.info("Deleting role with ID: {}", id);
        roleRepository.deleteById(id);
    }
}
