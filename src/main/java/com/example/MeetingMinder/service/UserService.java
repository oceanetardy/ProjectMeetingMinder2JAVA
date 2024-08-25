package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> findAll(Pageable pageable) {
        logger.info("Obtention de tous les utilisateurs avec pagination: {}", pageable);
        return userRepository.findAll(pageable);
    }

    public Optional<User> findById(Long id) {
        logger.info("Recherche de l'utilisateur avec ID: {}", id);
        return userRepository.findById(id);
    }

    public User save(User user) {
        logger.info("Sauvegarde de l'utilisateur: {}", user.getName());
        return userRepository.save(user);
    }

    public void deleteAll() {
        logger.info("Suppression de tous les utilisateurs");
        userRepository.deleteAll();
    }

    public void deleteById(Long id) {
        logger.info("Suppression de l'utilisateur avec ID: {}", id);
        userRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }
}
