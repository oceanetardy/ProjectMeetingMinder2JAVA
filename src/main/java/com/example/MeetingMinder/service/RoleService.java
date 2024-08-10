package com.example.MeetingMinder.service;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void deleteAll() {
        roleRepository.deleteAll();
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
