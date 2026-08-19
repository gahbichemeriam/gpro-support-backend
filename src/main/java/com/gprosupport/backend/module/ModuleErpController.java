package com.gprosupport.backend.module;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.module.dto.ModuleErpRequest;
import com.gprosupport.backend.module.dto.ModuleErpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ModuleErpController {

    private final ModuleErpService moduleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<List<ModuleErpResponse>>> findAll(
            @RequestParam(required = false) Long projetId) {
        return ResponseEntity.ok(ApiResponse.success("Modules récupérés.", moduleService.findAll(projetId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<ModuleErpResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Module trouvé.", moduleService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ModuleErpResponse>> create(@Valid @RequestBody ModuleErpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Module créé.", moduleService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD')")
    public ResponseEntity<ApiResponse<ModuleErpResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ModuleErpRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Module mis à jour.", moduleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
