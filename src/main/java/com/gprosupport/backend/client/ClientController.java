package com.gprosupport.backend.client;

import com.gprosupport.backend.client.dto.ClientRequest;
import com.gprosupport.backend.client.dto.ClientResponse;
import com.gprosupport.backend.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> findAll(
            @RequestParam(required = false) Long projetId) {
        return ResponseEntity.ok(ApiResponse.success("Clients récupérés.", clientService.findAll(projetId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RD','AGENT_SUPPORT')")
    public ResponseEntity<ApiResponse<ClientResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Client trouvé.", clientService.findById(id)));
    }

    /** Seul ADMIN gère le parc clients */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClientResponse>> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Client créé.", clientService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClientResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Client mis à jour.", clientService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
