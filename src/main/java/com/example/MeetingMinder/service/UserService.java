package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> findByName(String name, Pageable pageable) {
        return userRepository.findByNameContaining(name, pageable);
    }

    public Page<User> findByRoleName(String roleName, Pageable pageable) {
        return userRepository.findByRole_Name(roleName, pageable);
    }

    public Page<User> findByNameAndRoleName(String name, String roleName, Pageable pageable) {
        return userRepository.findByNameContainingAndRole_Name(name, roleName, pageable);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
