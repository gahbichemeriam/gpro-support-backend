package com.gprosupport.backend.version;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.version.dto.VersionErpRequest;
import com.gprosupport.backend.version.dto.VersionErpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class VersionErpController {

    private final VersionErpService versionService;

    /** GET /api/versions?projetId=1 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<VersionErpResponse>>> findAll(
            @RequestParam(required = false) Long projetId) {
        return ResponseEntity.ok(
            ApiResponse.success("Versions récupérées.", versionService.findAll(projetId))
        );
    }

    /** GET /api/versions/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VersionErpResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Version trouvée.", versionService.findById(id))
        );
    }

    /** POST /api/versions */
    @PostMapping
    public ResponseEntity<ApiResponse<VersionErpResponse>> create(
            @Valid @RequestBody VersionErpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Version créée avec succès.", versionService.create(request))
        );
    }

    /** PUT /api/versions/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VersionErpResponse>> update(
            @PathVariable Long id, @Valid @RequestBody VersionErpRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Version mise à jour.", versionService.update(id, request))
        );
    }

    /** DELETE /api/versions/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        versionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
