package com.example.MeetingMinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de la réservation", example = "1")
    private Long id;

    @NotNull(message = "La date de début est obligatoire")
    @Schema(description = "Date et heure de début de la réservation", example = "2024-08-25T10:00:00")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "La date de fin est obligatoire")
    @Schema(description = "Date et heure de fin de la réservation", example = "2024-08-25T12:00:00")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Schema(description = "Description de la réservation", example = "Réunion de projet")
    @Column(nullable = true)
    private String description;

    @Schema(description = "Date et heure de création de la réservation", example = "2024-08-01T08:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Schema(description = "Date et heure de la dernière mise à jour de la réservation", example = "2024-08-01T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Utilisateur ayant créé la réservation", implementation = User.class)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @Schema(description = "Salle réservée", implementation = Room.class)
    private Room room;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
