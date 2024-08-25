package com.example.MeetingMinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de la salle", example = "1")
    private Long id;

    @NotBlank(message = "Le nom de la salle est obligatoire")
    @Column(unique = true, nullable = false)
    @Schema(description = "Nom unique de la salle", example = "Salle de Conférence")
    private String name;

    @Min(value = 1, message = "La capacité doit être d'au moins 1 personne")
    @Schema(description = "Capacité de la salle", example = "50")
    private Integer capacity;

    @Schema(description = "Description de la salle", example = "Une salle équipée pour les présentations.")
    @Column()
    private String description;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
