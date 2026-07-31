package com.gprosupport.backend.probleme;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.probleme.dto.ProblemeRequest;
import com.gprosupport.backend.probleme.dto.ProblemeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problemes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProblemeController {

    private final ProblemeService problemeService;

    /**
     * GET /api/problemes              → tous les problèmes
     * GET /api/problemes?moduleId=1   → problèmes d'un module
     * GET /api/problemes?recherche=stock → recherche textuelle (parcours guidé)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProblemeResponse>>> findAll(
            @RequestParam(required = false) Long moduleId,
            @RequestParam(required = false) String recherche) {

        List<ProblemeResponse> result = (recherche != null && !recherche.isBlank())
                ? problemeService.rechercher(recherche)
                : problemeService.findAll(moduleId);

        return ResponseEntity.ok(ApiResponse.success("Problèmes récupérés.", result));
    }

    /** GET /api/problemes/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProblemeResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Problème trouvé.", problemeService.findById(id))
        );
    }

    /** POST /api/problemes */
    @PostMapping
    public ResponseEntity<ApiResponse<ProblemeResponse>> create(
            @Valid @RequestBody ProblemeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Problème créé avec succès.", problemeService.create(request))
        );
    }

    /** PUT /api/problemes/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProblemeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProblemeRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Problème mis à jour.", problemeService.update(id, request))
        );
    }

    /** DELETE /api/problemes/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
