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
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Gestion des rôles", description = "Opérations liées à la gestion des rôles dans l'API")
public class RoleController {

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
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
        Role savedRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
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
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            Role updatedRole = role.get();
            updatedRole.setName(roleDetails.getName());
            return ResponseEntity.ok(roleService.save(updatedRole));
        } else {
            return ResponseEntity.notFound().build();
        }
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
        if (roleService.findById(id).isPresent()) {
            roleService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
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
        roleService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
