package com.gprosupport.backend.resolution;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.resolution.dto.ResolutionRequest;
import com.gprosupport.backend.resolution.dto.ResolutionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resolutions")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ResolutionController {

    private final ResolutionService resolutionService;

    /**
     * GET /api/resolutions               → toutes les résolutions
     * GET /api/resolutions?problemeId=1  → résolutions d'un problème précis
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResolutionResponse>>> findAll(
            @RequestParam(required = false) Long problemeId) {
        return ResponseEntity.ok(
            ApiResponse.success("Résolutions récupérées.", resolutionService.findAll(problemeId))
        );
    }

    /** GET /api/resolutions/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResolutionResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Résolution trouvée.", resolutionService.findById(id))
        );
    }

    /** POST /api/resolutions */
    @PostMapping
    public ResponseEntity<ApiResponse<ResolutionResponse>> create(
            @Valid @RequestBody ResolutionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Résolution créée avec succès.", resolutionService.create(request))
        );
    }

    /** PUT /api/resolutions/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResolutionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ResolutionRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Résolution mise à jour.", resolutionService.update(id, request))
        );
    }

    /**
     * PATCH /api/resolutions/{id}/valider
     * Valide une résolution (approuvée par QA).
     * PATCH = modification partielle d'une ressource (juste le champ validationQa ici).
     */
    @PatchMapping("/{id}/valider")
    public ResponseEntity<ApiResponse<ResolutionResponse>> valider(@PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Résolution validée par QA.", resolutionService.valider(id))
        );
    }

    /** DELETE /api/resolutions/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resolutionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
