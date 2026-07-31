package com.gprosupport.backend.module;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.module.dto.ModuleErpRequest;
import com.gprosupport.backend.module.dto.ModuleErpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ModuleErpController {

    private final ModuleErpService moduleService;

    /**
     * GET /api/modules          → tous les modules
     * GET /api/modules?projetId=1 → modules d'un projet précis (parcours guidé)
     *
     * @RequestParam(required = false) → le paramètre est optionnel dans l'URL
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ModuleErpResponse>>> findAll(
            @RequestParam(required = false) Long projetId) {
        return ResponseEntity.ok(
            ApiResponse.success("Modules récupérés avec succès.", moduleService.findAll(projetId))
        );
    }

    /** GET /api/modules/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleErpResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Module trouvé.", moduleService.findById(id))
        );
    }

    /** POST /api/modules */
    @PostMapping
    public ResponseEntity<ApiResponse<ModuleErpResponse>> create(
            @Valid @RequestBody ModuleErpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Module créé avec succès.", moduleService.create(request))
        );
    }

    /** PUT /api/modules/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleErpResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ModuleErpRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Module mis à jour avec succès.", moduleService.update(id, request))
        );
    }

    /** DELETE /api/modules/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
