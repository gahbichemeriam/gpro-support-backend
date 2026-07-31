package com.gprosupport.backend.module;

import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.module.dto.ModuleErpRequest;
import com.gprosupport.backend.module.dto.ModuleErpResponse;
import com.gprosupport.backend.projet.ProjetErp;
import com.gprosupport.backend.projet.ProjetErpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModuleErpService {

    private final ModuleErpRepository moduleRepository;
    private final ProjetErpRepository projetRepository;

    /** Liste tous les modules, ou filtre par projet si projetId fourni. */
    @Transactional(readOnly = true)
    public List<ModuleErpResponse> findAll(Long projetId) {
        List<ModuleErp> modules = (projetId != null)
                ? moduleRepository.findByProjetErpId(projetId)
                : moduleRepository.findAllWithProjet();

        return modules.stream().map(this::toResponse).toList();
    }

    /** Récupère un module par son id. */
    @Transactional(readOnly = true)
    public ModuleErpResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    /** Crée un nouveau module dans un projet existant. */
    public ModuleErpResponse create(ModuleErpRequest request) {
        // Vérifie que le projet parent existe
        ProjetErp projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet", request.getProjetId()));

        ModuleErp module = ModuleErp.builder()
                .projetErp(projet)
                .nom(request.getNom())
                .description(request.getDescription())
                .build();

        return toResponse(moduleRepository.save(module));
    }

    /** Met à jour un module existant. */
    public ModuleErpResponse update(Long id, ModuleErpRequest request) {
        ModuleErp module = getOrThrow(id);

        // Si le projetId change, vérifie que le nouveau projet existe
        if (!module.getProjetErp().getId().equals(request.getProjetId())) {
            ProjetErp nouveauProjet = projetRepository.findById(request.getProjetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Projet", request.getProjetId()));
            module.setProjetErp(nouveauProjet);
        }

        module.setNom(request.getNom());
        module.setDescription(request.getDescription());

        return toResponse(module);
    }

    /** Supprime un module. */
    public void delete(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Module", id);
        }
        moduleRepository.deleteById(id);
    }

    // ---- méthodes privées utilitaires ----

    private ModuleErp getOrThrow(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module", id));
    }

    private ModuleErpResponse toResponse(ModuleErp module) {
        return ModuleErpResponse.builder()
                .id(module.getId())
                .nom(module.getNom())
                .description(module.getDescription())
                .projetId(module.getProjetErp().getId())
                .projetNom(module.getProjetErp().getNom())
                .build();
    }
}
