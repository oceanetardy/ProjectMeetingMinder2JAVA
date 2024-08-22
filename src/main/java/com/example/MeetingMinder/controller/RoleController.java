package com.example.MeetingMinder.controller;

import com.example.MeetingMinder.model.Role;
import com.example.MeetingMinder.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Management", description = "Gestion des rôles dans l'application")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Obtenir un rôle par son identifiant", description = "Retourne un rôle en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "Rôle trouvé",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtenir tous les rôles", description = "Retourne une liste paginée de tous les rôles")
    @ApiResponse(responseCode = "200", description = "Liste des rôles récupérée avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleService.findAll(pageable);
    }

    @Operation(summary = "Créer un nouveau rôle", description = "Crée un nouveau rôle et retourne le rôle créé.")
    @ApiResponse(responseCode = "201", description = "Rôle créé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)))
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role savedRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @Operation(summary = "Mettre à jour un rôle par son identifiant", description = "Met à jour un rôle existant et retourne le rôle mis à jour.")
    @ApiResponse(responseCode = "200", description = "Rôle mis à jour avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class)))
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role roleDetails) {
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            Role updatedRole = role.get();
            updatedRole.setName(roleDetails.getName());
            return ResponseEntity.ok(roleService.save(updatedRole));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer un rôle par son identifiant", description = "Supprime un rôle en fonction de son ID.")
    @ApiResponse(responseCode = "204", description = "Rôle supprimé avec succès")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable Long id) {
        if (roleService.findById(id).isPresent()) {
            roleService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer tous les rôles", description = "Supprime tous les rôles.")
    @ApiResponse(responseCode = "204", description = "Tous les rôles ont été supprimés avec succès")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllRoles() {
        roleService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
