package com.gprosupport.backend.probleme;

import com.gprosupport.backend.common.exception.BusinessException;
import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.module.ModuleErp;
import com.gprosupport.backend.module.ModuleErpRepository;
import com.gprosupport.backend.probleme.dto.ProblemeRequest;
import com.gprosupport.backend.probleme.dto.ProblemeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemeService {

    private final ProblemeRepository problemeRepository;
    private final ModuleErpRepository moduleRepository;

    /**
     * Charge le problème avec ses relations (module + projet) en une seule requête SQL.
     * Évite le problème LazyInitializationException :
     * sans ça, accéder à p.getModuleErp().getProjetErp() hors transaction plante.
     */

    /** Liste tous les problèmes, avec filtre optionnel par module. */
    @Transactional(readOnly = true)
    public List<ProblemeResponse> findAll(Long moduleId) {
        List<ProblemeFonctionnalite> problemes = (moduleId != null)
                ? problemeRepository.findByModuleErpId(moduleId)
                : problemeRepository.findAllWithRelations();
        return problemes.stream().map(this::toResponse).toList();
    }

    /** Récupère un problème par son id. */
    @Transactional(readOnly = true)
    public ProblemeResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    /**
     * Recherche textuelle — utilisée dans le parcours guidé de l'agent.
     * Cherche dans le titre ET le code erreur.
     */
    @Transactional(readOnly = true)
    public List<ProblemeResponse> rechercher(String terme) {
        return problemeRepository.rechercher(terme)
                .stream().map(this::toResponse).toList();
    }

    /** Crée un nouveau problème. */
    public ProblemeResponse create(ProblemeRequest request) {
        // Vérifie que le module existe
        ModuleErp module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module", request.getModuleId()));

        // Vérifie l'unicité du code erreur
        if (problemeRepository.findByCodeErreur(request.getCodeErreur()).isPresent()) {
            throw new BusinessException(
                "Le code erreur '" + request.getCodeErreur() + "' existe déjà."
            );
        }

        ProblemeFonctionnalite probleme = ProblemeFonctionnalite.builder()
                .moduleErp(module)
                .titre(request.getTitre())
                .codeErreur(request.getCodeErreur())
                .priorite(request.getPriorite() != null ? request.getPriorite() : PrioriteProbleme.MOYENNE)
                .build();

        return toResponse(problemeRepository.save(probleme));
    }

    /** Met à jour un problème existant. */
    public ProblemeResponse update(Long id, ProblemeRequest request) {
        ProblemeFonctionnalite probleme = getOrThrow(id);

        // Vérifie l'unicité du code erreur s'il change
        if (!probleme.getCodeErreur().equals(request.getCodeErreur())
                && problemeRepository.findByCodeErreur(request.getCodeErreur()).isPresent()) {
            throw new BusinessException(
                "Le code erreur '" + request.getCodeErreur() + "' existe déjà."
            );
        }

        if (!probleme.getModuleErp().getId().equals(request.getModuleId())) {
            ModuleErp module = moduleRepository.findById(request.getModuleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Module", request.getModuleId()));
            probleme.setModuleErp(module);
        }

        probleme.setTitre(request.getTitre());
        probleme.setCodeErreur(request.getCodeErreur());
        if (request.getPriorite() != null) probleme.setPriorite(request.getPriorite());

        return toResponse(probleme);
    }

    /** Supprime un problème. */
    public void delete(Long id) {
        if (!problemeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Problème", id);
        }
        problemeRepository.deleteById(id);
    }

    private ProblemeFonctionnalite getOrThrow(Long id) {
        return problemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problème", id));
    }

    private ProblemeResponse toResponse(ProblemeFonctionnalite p) {
        return ProblemeResponse.builder()
                .id(p.getId())
                .titre(p.getTitre())
                .codeErreur(p.getCodeErreur())
                .priorite(p.getPriorite())
                .dateCreation(p.getDateCreation())
                .moduleId(p.getModuleErp().getId())
                .moduleNom(p.getModuleErp().getNom())
                .projetId(p.getModuleErp().getProjetErp().getId())
                .projetNom(p.getModuleErp().getProjetErp().getNom())
                .build();
    }
}
