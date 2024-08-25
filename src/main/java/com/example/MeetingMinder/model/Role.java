package com.example.MeetingMinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique du rôle", example = "1")
    private Long id;

    @NotBlank(message = "Le nom du rôle est obligatoire")
    @Column(unique = true, nullable = false)
    @Schema(description = "Nom unique du rôle", example = "Admin")
    private String name;

    // Getter pour l'identifiant du rôle
    public Long getId() {
        return id;
    }

    // Setter pour l'identifiant du rôle
    public void setId(Long id) {
        this.id = id;
    }

    // Getter pour le nom du rôle
    public String getName() {
        return name;
    }

    // Setter pour le nom du rôle
    public void setName(String name) {
        this.name = name;
    }
}
