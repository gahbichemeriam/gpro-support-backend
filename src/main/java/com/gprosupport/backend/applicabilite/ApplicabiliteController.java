package com.gprosupport.backend.applicabilite;

import com.gprosupport.backend.applicabilite.dto.AlerteVersionResponse;
import com.gprosupport.backend.applicabilite.dto.ApplicabiliteRequest;
import com.gprosupport.backend.applicabilite.dto.ApplicabiliteResponse;
import com.gprosupport.backend.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applicabilites")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ApplicabiliteController {

    private final ApplicabiliteService applicabiliteService;

    /** GET /api/applicabilites?problemeId=1 → matrice d'un problème */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicabiliteResponse>>> findAll(
            @RequestParam(required = false) Long problemeId,
            @RequestParam(required = false) Long versionId) {

        List<ApplicabiliteResponse> result = (problemeId != null)
                ? applicabiliteService.findByProbleme(problemeId)
                : applicabiliteService.findByVersion(versionId);

        return ResponseEntity.ok(ApiResponse.success("Matrice récupérée.", result));
    }

    /** POST /api/applicabilites */
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicabiliteResponse>> create(
            @Valid @RequestBody ApplicabiliteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Entrée matrice créée.", applicabiliteService.create(request))
        );
    }

    /**
     * GET /api/applicabilites/alertes?problemeId=1
     * Retourne la liste des clients impactés par ce bug et qui doivent mettre à jour.
     */
    @GetMapping("/alertes")
    public ResponseEntity<ApiResponse<List<AlerteVersionResponse>>> verifierAlertes(
            @RequestParam Long problemeId) {
        List<AlerteVersionResponse> alertes = applicabiliteService.verifierAlertes(problemeId);
        String message = alertes.isEmpty()
                ? "Aucun client impacté détecté."
                : alertes.size() + " client(s) nécessitent une mise à jour.";
        return ResponseEntity.ok(ApiResponse.success(message, alertes));
    }

    /** DELETE /api/applicabilites/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicabiliteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
