package com.example.MeetingMinder.repository;

import com.example.MeetingMinder.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findByNameContaining(String name, Pageable pageable);
}
