package com.gprosupport.backend.rapport;

import com.gprosupport.backend.client.ClientRepository;
import com.gprosupport.backend.module.ModuleErpRepository;
import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import com.gprosupport.backend.probleme.ProblemeRepository;
import com.gprosupport.backend.projet.ProjetErpRepository;
import com.gprosupport.backend.resolution.ResolutionRepository;
import com.gprosupport.backend.version.VersionErpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RapportService {

    private final ProblemeRepository problemeRepository;
    private final ResolutionRepository resolutionRepository;
    private final ClientRepository clientRepository;
    private final VersionErpRepository versionRepository;
    private final ProjetErpRepository projetRepository;
    private final ModuleErpRepository moduleRepository;

    /**
     * Top N problèmes classés par nombre de résolutions.
     * Plus un problème a de résolutions = plus il est complexe / fréquent.
     */
    public List<TopPanneDTO> getTopPannes(int limit) {
        List<ProblemeFonctionnalite> tousLesProblemes =
                problemeRepository.findAllWithRelations();

        return tousLesProblemes.stream()
                .map(p -> {
                    long nbRes = resolutionRepository.findByProblemeId(p.getId()).size();
                    boolean qaOk = resolutionRepository
                            .findByProblemeIdAndValidationQaTrue(p.getId()).size() > 0;
                    return new TopPanneDTO(
                            p.getId(),
                            p.getCodeErreur(),
                            p.getTitre(),
                            p.getModuleErp().getNom(),
                            p.getPriorite().name(),
                            nbRes,
                            qaOk
                    );
                })
                // Trier : d'abord les CRITIQUE, puis HAUTE, puis par nb résolutions
                .sorted(Comparator
                        .comparing((TopPanneDTO t) -> prioriteOrdre(t.getPriorite()))
                        .reversed()
                        .thenComparing(Comparator.comparingLong(TopPanneDTO::getNbResolutions).reversed()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * KPI globaux du support.
     */
    public KpiDTO getKpi() {
        long totalProblemes = problemeRepository.count();
        long totalResolutions = resolutionRepository.count();
        long resolutionsValidees = resolutionRepository.findAll()
                .stream().filter(r -> Boolean.TRUE.equals(r.getValidationQa())).count();
        long resolutionsEnAttente = totalResolutions - resolutionsValidees;

        // Problèmes avec au moins une résolution
        long avecResolution = problemeRepository.findAllWithRelations()
                .stream()
                .filter(p -> !resolutionRepository.findByProblemeId(p.getId()).isEmpty())
                .count();

        long sansSolution = totalProblemes - avecResolution;

        double tauxResolution = totalProblemes > 0
                ? Math.round((double) avecResolution / totalProblemes * 100.0 * 10) / 10.0
                : 0.0;

        double tauxQa = totalResolutions > 0
                ? Math.round((double) resolutionsValidees / totalResolutions * 100.0 * 10) / 10.0
                : 0.0;

        return KpiDTO.builder()
                .totalProblemes(totalProblemes)
                .problemesAvecResolution(avecResolution)
                .problemesSansResolution(sansSolution)
                .resolutionsValidees(resolutionsValidees)
                .resolutionsEnAttente(resolutionsEnAttente)
                .tauxResolution(tauxResolution)
                .tauxValidationQa(tauxQa)
                .totalClients(clientRepository.count())
                .totalVersions(versionRepository.count())
                .totalProjets(projetRepository.count())
                .totalModules(moduleRepository.count())
                .build();
    }

    /**
     * Nombre de problèmes par module.
     */
    public List<Map<String, Object>> getStatsParModule() {
        return problemeRepository.findAllWithRelations()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getModuleErp().getNom(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("module", e.getKey());
                    m.put("count", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    /**
     * Répartition des problèmes par priorité.
     */
    public List<Map<String, Object>> getStatsParPriorite() {
        return problemeRepository.findAllWithRelations()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPriorite().name(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("priorite", e.getKey());
                    m.put("count", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    private int prioriteOrdre(String priorite) {
        return switch (priorite) {
            case "CRITIQUE" -> 4;
            case "HAUTE"    -> 3;
            case "MOYENNE"  -> 2;
            default         -> 1;
        };
    }
}
