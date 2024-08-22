package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Gestion des utilisateurs dans l'application")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtenir tous les utilisateurs", description = "Retourne une liste paginée de tous les utilisateurs")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Operation(summary = "Obtenir un utilisateur par son identifiant", description = "Retourne un utilisateur en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "Utilisateur trouvé",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer un nouvel utilisateur", description = "Crée un nouvel utilisateur et retourne l'utilisateur créé")
    @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour un utilisateur existant et retourne l'utilisateur mis à jour")
    @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setName(userDetails.getName());
            updatedUser.setPassword(userDetails.getPassword());
            updatedUser.setRole(userDetails.getRole());
            return ResponseEntity.ok(userService.save(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer un utilisateur par son identifiant", description = "Supprime un utilisateur en fonction de son ID")
    @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer tous les utilisateurs", description = "Supprime tous les utilisateurs")
    @ApiResponse(responseCode = "204", description = "Tous les utilisateurs ont été supprimés avec succès")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
