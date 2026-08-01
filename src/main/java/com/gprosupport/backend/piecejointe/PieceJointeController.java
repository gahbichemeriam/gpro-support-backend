package com.gprosupport.backend.piecejointe;

import com.gprosupport.backend.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pieces-jointes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PieceJointeController {

    private final PieceJointeService pieceJointeService;

    /**
     * GET /api/pieces-jointes?resolutionId=1
     * Liste les fichiers d'une résolution.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PieceJointe>>> findByResolution(
            @RequestParam Long resolutionId) {
        return ResponseEntity.ok(
            ApiResponse.success("Pièces jointes récupérées.",
                pieceJointeService.findByResolution(resolutionId))
        );
    }

    /**
     * POST /api/pieces-jointes/upload?resolutionId=1
     * Upload d'un fichier joint à une résolution.
     *
     * @RequestParam("fichier") MultipartFile → Spring lit le fichier
     * depuis la requête multipart/form-data.
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<PieceJointe>> upload(
            @RequestParam Long resolutionId,
            @RequestParam("fichier") MultipartFile fichier) {
        PieceJointe saved = pieceJointeService.upload(resolutionId, fichier);
        return ResponseEntity.ok(
            ApiResponse.success("Fichier uploadé avec succès.", saved)
        );
    }

    /**
     * GET /api/pieces-jointes/{id}/telecharger
     * Téléchargement d'un fichier.
     *
     * Content-Disposition: attachment → force le téléchargement dans le navigateur.
     */
    @GetMapping("/{id}/telecharger")
    public ResponseEntity<Resource> telecharger(@PathVariable Long id) {
        Resource resource = pieceJointeService.telecharger(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /** DELETE /api/pieces-jointes/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pieceJointeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
