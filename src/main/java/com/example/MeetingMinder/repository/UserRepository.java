package com.example.MeetingMinder.repository;

import com.example.MeetingMinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameContaining(String name, Pageable pageable);
    Page<User> findByRole_Name(String roleName, Pageable pageable);
    Page<User> findByNameContainingAndRole_Name(String name, String roleName, Pageable pageable);
}

