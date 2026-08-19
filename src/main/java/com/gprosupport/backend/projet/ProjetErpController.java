package com.gprosupport.backend.projet;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.projet.dto.ProjetErpRequest;
import com.gprosupport.backend.projet.dto.ProjetErpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la gestion des Projets ERP.
 *
 * @RestController → combine @Controller + @ResponseBody
 *   Chaque méthode retourne directement du JSON (pas une vue HTML).
 *
 * @RequestMapping("/api/projets") → toutes les routes de ce controller
 *   commencent par /api/projets
 *
 * @CrossOrigin → autorise Angular (localhost:4200) à appeler cette API.
 *   Nécessaire car le frontend et le backend sont sur des ports différents.
 *
 * Convention des codes HTTP utilisés :
 *   200 OK          → GET réussi
 *   201 Created     → POST réussi (ressource créée)
 *   204 No Content  → DELETE réussi (rien à retourner)
 *   400 Bad Request → données invalides
 *   404 Not Found   → ressource introuvable
 */
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProjetErpController {

    private final ProjetErpService projetService;

    /** Tous les rôles peuvent lire */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<List<ProjetErpResponse>>> findAll() {
        List<ProjetErpResponse> projets = projetService.findAll();
        return ResponseEntity.ok(
            ApiResponse.success("Projets récupérés avec succès.", projets)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<ProjetErpResponse>> findById(@PathVariable Long id) {
        ProjetErpResponse projet = projetService.findById(id);
        return ResponseEntity.ok(
            ApiResponse.success("Projet trouvé.", projet)
        );
    }

    /** Seul ADMIN peut créer/modifier/supprimer des projets */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProjetErpResponse>> create(
            @Valid @RequestBody ProjetErpRequest request) {
        ProjetErpResponse created = projetService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Projet créé avec succès.", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProjetErpResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjetErpRequest request) {
        ProjetErpResponse updated = projetService.update(id, request);
        return ResponseEntity.ok(
            ApiResponse.success("Projet mis à jour avec succès.", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        projetService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
