package com.gprosupport.backend.client;

import com.gprosupport.backend.client.dto.ClientRequest;
import com.gprosupport.backend.client.dto.ClientResponse;
import com.gprosupport.backend.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /** GET /api/clients?projetId=1 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponse>>> findAll(
            @RequestParam(required = false) Long projetId) {
        return ResponseEntity.ok(
            ApiResponse.success("Clients récupérés.", clientService.findAll(projetId))
        );
    }

    /** GET /api/clients/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ApiResponse.success("Client trouvé.", clientService.findById(id))
        );
    }

    /** POST /api/clients */
    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponse>> create(
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success("Client créé avec succès.", clientService.create(request))
        );
    }

    /** PUT /api/clients/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Client mis à jour.", clientService.update(id, request))
        );
    }

    /** DELETE /api/clients/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
