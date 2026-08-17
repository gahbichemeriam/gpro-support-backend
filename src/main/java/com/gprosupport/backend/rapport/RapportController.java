package com.gprosupport.backend.rapport;

import com.gprosupport.backend.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST pour les rapports et statistiques.
 * Répond aux besoins du CDC :
 * - Top pannes par module
 * - KPI support (MTTR, FCR, tickets ouverts/clôturés)
 * - Matrice de compatibilité
 */
@RestController
@RequestMapping("/api/rapports")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RapportController {

    private final RapportService rapportService;

    /**
     * GET /api/rapports/top-pannes
     * Les problèmes les plus fréquents par module.
     */
    @GetMapping("/top-pannes")
    public ResponseEntity<ApiResponse<List<TopPanneDTO>>> getTopPannes(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(
            ApiResponse.success("Top pannes récupéré.", rapportService.getTopPannes(limit))
        );
    }

    /**
     * GET /api/rapports/kpi
     * Indicateurs clés de performance du support.
     */
    @GetMapping("/kpi")
    public ResponseEntity<ApiResponse<KpiDTO>> getKpi() {
        return ResponseEntity.ok(
            ApiResponse.success("KPI récupérés.", rapportService.getKpi())
        );
    }

    /**
     * GET /api/rapports/par-module
     * Nombre de problèmes par module.
     */
    @GetMapping("/par-module")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getParModule() {
        return ResponseEntity.ok(
            ApiResponse.success("Stats par module.", rapportService.getStatsParModule())
        );
    }

    /**
     * GET /api/rapports/par-priorite
     * Répartition des problèmes par priorité.
     */
    @GetMapping("/par-priorite")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getParPriorite() {
        return ResponseEntity.ok(
            ApiResponse.success("Stats par priorité.", rapportService.getStatsParPriorite())
        );
    }
}
