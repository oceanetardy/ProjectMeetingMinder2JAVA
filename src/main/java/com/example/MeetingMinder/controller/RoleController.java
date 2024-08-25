package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.service.RoleService;
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
@RequestMapping("/api/roles")
@Tag(name = "Gestion des rôles", description = "Opérations liées à la gestion des rôles dans l'API")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Obtenir un rôle par ID",
            description = "Récupère les détails d'un rôle spécifique à partir de son identifiant unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle trouvé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(
            @Parameter(description = "Identifiant unique du rôle recherché", example = "1")
            @PathVariable Long id) {
        logger.info("Requête pour obtenir le rôle avec ID: {}", id);
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            logger.info("Rôle trouvé: {}", role.get().getName());
            return ResponseEntity.ok(role.get());
        } else {
            logger.warn("Rôle avec ID: {} non trouvé", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtenir tous les rôles avec pagination",
            description = "Récupère une liste paginée de tous les rôles disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rôles récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public Page<Role> getAllRoles(
            @Parameter(description = "Paramètres de pagination") Pageable pageable) {
        logger.info("Requête pour obtenir tous les rôles avec pagination: {}", pageable);
        return roleService.findAll(pageable);
    }

    @Operation(summary = "Créer un nouveau rôle",
            description = "Crée un nouveau rôle avec les détails fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rôle créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Role> createRole(
            @Parameter(description = "Détails du rôle à créer", required = true)
            @Valid @RequestBody Role role) {
        logger.info("Requête pour créer un nouveau rôle avec nom: {}", role.getName());
        try {
            Role savedRole = roleService.save(role);
            logger.info("Rôle créé avec succès: {}", savedRole.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du rôle: {}", role.getName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Mettre à jour un rôle par ID",
            description = "Met à jour un rôle existant avec les nouvelles informations fournies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(
            @Parameter(description = "Identifiant unique du rôle à mettre à jour", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du rôle", required = true)
            @Valid @RequestBody Role roleDetails) {
        logger.info("Requête pour mettre à jour le rôle avec ID: {}", id);
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            Role updatedRole = role.get();
            updatedRole.setName(roleDetails.getName());
            Role savedRole = roleService.save(updatedRole);
            logger.info("Rôle mis à jour avec succès: {}", savedRole.getName());
            return ResponseEntity.ok(savedRole);
        } else {
            logger.warn("Rôle avec ID: {} non trouvé pour mise à jour", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Mettre à jour partiellement un rôle par ID",
            description = "Met à jour partiellement un rôle existant avec les informations fournies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rôle mis à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Role> partialUpdateRole(
            @Parameter(description = "Identifiant unique du rôle à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Informations à mettre à jour", required = true)
            @RequestBody Map<String, Object> updates) {
        logger.info("Requête pour mise à jour partielle du rôle avec ID: {}", id);
        Optional<Role> roleOptional = roleService.findById(id);

        if (roleOptional.isEmpty()) {
            logger.warn("Rôle avec ID: {} non trouvé pour mise à jour partielle", id);
            return ResponseEntity.notFound().build();
        }

        Role role = roleOptional.get();

        updates.forEach((key, value) -> {
            logger.info("Mise à jour du champ: {} avec la valeur: {}", key, value);
            switch (key) {
                case "name":
                    role.setName((String) value);
                    break;
            }
        });

        Role updatedRole = roleService.save(role);
        logger.info("Mise à jour partielle réussie pour le rôle: {}", updatedRole.getName());
        return ResponseEntity.ok(updatedRole);
    }

    @Operation(summary = "Supprimer un rôle par ID",
            description = "Supprime un rôle spécifique en utilisant son identifiant unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rôle supprimé avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(
            @Parameter(description = "Identifiant unique du rôle à supprimer", example = "1")
            @PathVariable Long id) {
        logger.info("Requête pour supprimer le rôle avec ID: {}", id);
        if (roleService.findById(id).isPresent()) {
            roleService.deleteById(id);
            logger.info("Rôle avec ID: {} supprimé avec succès", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Rôle avec ID: {} non trouvé pour suppression", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer tous les rôles",
            description = "Supprime tous les rôles existants dans le système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tous les rôles ont été supprimés avec succès", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAllRoles() {
        logger.info("Requête pour supprimer tous les rôles");
        roleService.deleteAll();
        logger.info("Tous les rôles ont été supprimés avec succès");
        return ResponseEntity.noContent().build();
    }
}
