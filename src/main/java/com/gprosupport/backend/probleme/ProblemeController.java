package com.gprosupport.backend.probleme;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.probleme.dto.ProblemeRequest;
import com.gprosupport.backend.probleme.dto.ProblemeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problemes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProblemeController {

    private final ProblemeService problemeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<List<ProblemeResponse>>> findAll(
            @RequestParam(required = false) Long moduleId,
            @RequestParam(required = false) String recherche) {
        List<ProblemeResponse> result = (recherche != null && !recherche.isBlank())
                ? problemeService.rechercher(recherche)
                : problemeService.findAll(moduleId);
        return ResponseEntity.ok(ApiResponse.success("Problèmes récupérés.", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<ProblemeResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Problème trouvé.", problemeService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ProblemeResponse>> create(@Valid @RequestBody ProblemeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Problème créé.", problemeService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ProblemeResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ProblemeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Problème mis à jour.", problemeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        problemeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
