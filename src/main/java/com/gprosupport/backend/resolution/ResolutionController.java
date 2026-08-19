package com.gprosupport.backend.resolution;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.resolution.dto.ResolutionRequest;
import com.gprosupport.backend.resolution.dto.ResolutionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resolutions")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ResolutionController {

    private final ResolutionService resolutionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<List<ResolutionResponse>>> findAll(
            @RequestParam(required = false) Long problemeId) {
        return ResponseEntity.ok(ApiResponse.success("Résolutions récupérées.", resolutionService.findAll(problemeId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<ResolutionResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Résolution trouvée.", resolutionService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ResolutionResponse>> create(@Valid @RequestBody ResolutionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Résolution créée.", resolutionService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ResolutionResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ResolutionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Résolution mise à jour.", resolutionService.update(id, request)));
    }

    /** Validation QA — ADMIN et RD seulement */
    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ResolutionResponse>> valider(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Résolution validée par QA.", resolutionService.valider(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resolutionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
