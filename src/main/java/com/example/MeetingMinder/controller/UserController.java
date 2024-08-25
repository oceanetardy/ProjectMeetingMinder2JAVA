package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.model.User;
import com.example.MeetingMinder.service.RoleService;
import com.example.MeetingMinder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Gestion des utilisateurs", description = "Opérations liées à la gestion des utilisateurs dans l'application")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Operation(summary = "Obtenir tous les utilisateurs",
            description = "Retourne une liste paginée de tous les utilisateurs")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping
    public Page<User> getAllUsers(Pageable pageable) {
        logger.info("Requête pour obtenir tous les utilisateurs avec pagination: {}", pageable);
        return userService.findAll(pageable);
    }

    @Operation(summary = "Obtenir un utilisateur par ID",
            description = "Retourne un utilisateur en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID de l'utilisateur à récupérer", example = "1") @PathVariable Long id) {
        logger.info("Requête pour obtenir l'utilisateur avec ID: {}", id);
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            logger.info("Utilisateur trouvé: {}", user.get().getName());
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("Utilisateur avec ID: {} non trouvé", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Créer un nouvel utilisateur",
            description = "Crée un nouvel utilisateur et retourne l'utilisateur créé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "Détails de l'utilisateur à créer", required = true)
            @Valid @RequestBody User user) {
        logger.info("Requête pour créer un nouvel utilisateur avec nom: {}", user.getName());
        try {
            User createdUser = userService.save(user);
            logger.info("Utilisateur créé avec succès: {}", createdUser.getName());
            return ResponseEntity.status(201).body(createdUser);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur: {}", user.getName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Mettre à jour un utilisateur par ID",
            description = "Met à jour un utilisateur existant avec les nouvelles informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Nouvelles informations de l'utilisateur", required = true)
            @Valid @RequestBody User userDetails) {
        logger.info("Requête pour mettre à jour l'utilisateur avec ID: {}", id);
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setName(userDetails.getName());
            updatedUser.setPassword(userDetails.getPassword());
            updatedUser.setRole(userDetails.getRole());
            User savedUser = userService.save(updatedUser);
            logger.info("Utilisateur mis à jour avec succès: {}", savedUser.getName());
            return ResponseEntity.ok(savedUser);
        } else {
            logger.warn("Utilisateur avec ID: {} non trouvé pour mise à jour", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour partiellement un utilisateur par ID",
            description = "Met à jour partiellement un utilisateur existant avec les informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdateUser(
            @Parameter(description = "ID de l'utilisateur à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour", required = true)
            @RequestBody Map<String, Object> updates) {
        logger.info("Requête pour mise à jour partielle de l'utilisateur avec ID: {}", id);
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            logger.warn("Utilisateur avec ID: {} non trouvé pour mise à jour partielle", id);
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        updates.forEach((key, value) -> {
            logger.info("Mise à jour du champ: {} avec la valeur: {}", key, value);
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "password":
                    user.setPassword((String) value);
                    break;
                case "role":
                    if (value instanceof Map<?, ?> roleMap) {
                        if (roleMap.containsKey("id")) {
                            Object roleIdObj = roleMap.get("id");
                            if (roleIdObj instanceof Number) {
                                Long role_id = ((Number) roleIdObj).longValue();
                                Role role = roleService.findById(role_id).orElse(null);
                                if (role == null) {
                                    logger.warn("Rôle avec ID: {} non trouvé", role_id);
                                    throw new RuntimeException("Rôle non trouvé");
                                }
                                user.setRole(role);
                            } else {
                                logger.warn("L'ID du rôle n'est pas un type numérique valide");
                                throw new RuntimeException("ID de rôle invalide");
                            }
                        }
                    }
                    break;
            }
        });

        User updatedUser = userService.save(user);
        logger.info("Mise à jour partielle réussie pour l'utilisateur: {}", updatedUser.getName());
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Supprimer un utilisateur par ID",
            description = "Supprime un utilisateur en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @Parameter(description = "ID de l'utilisateur à supprimer", example = "1") @PathVariable Long id) {
        logger.info("Requête pour supprimer l'utilisateur avec ID: {}", id);
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            logger.info("Utilisateur avec ID: {} supprimé avec succès", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Utilisateur avec ID: {} non trouvé pour suppression", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer tous les utilisateurs",
            description = "Supprime tous les utilisateurs existants dans le système")
    @ApiResponse(responseCode = "204", description = "Tous les utilisateurs ont été supprimés avec succès", content = @Content)
    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        logger.info("Requête pour supprimer tous les utilisateurs");
        userService.deleteAll();
        logger.info("Tous les utilisateurs ont été supprimés avec succès");
        return ResponseEntity.noContent().build();
    }
}
