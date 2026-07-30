package com.gprosupport.backend.projet;

import com.gprosupport.backend.common.exception.BusinessException;
import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.projet.dto.ProjetErpRequest;
import com.gprosupport.backend.projet.dto.ProjetErpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service métier pour la gestion des Projets ERP.
 *
 * @Service → Spring crée une instance unique de cette classe (Singleton).
 * @RequiredArgsConstructor → Lombok génère un constructeur avec tous les champs "final".
 *   C'est l'injection de dépendances par constructeur — meilleure pratique vs @Autowired.
 * @Transactional → chaque méthode s'exécute dans une transaction base de données.
 *   Si une erreur survient, tout est annulé (rollback automatique).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjetErpService {

    // Injection par constructeur (générée par @RequiredArgsConstructor)
    private final ProjetErpRepository projetRepository;

    /**
     * Récupère tous les projets ERP.
     * @Transactional(readOnly = true) → optimisation : pas de suivi des changements.
     */
    @Transactional(readOnly = true)
    public List<ProjetErpResponse> findAll() {
        return projetRepository.findAll()
                .stream()
                .map(this::toResponse)   // convertit chaque entité en DTO
                .toList();
    }

    /**
     * Récupère un projet par son id.
     * orElseThrow → si absent, lève ResourceNotFoundException → HTTP 404 automatiquement.
     */
    @Transactional(readOnly = true)
    public ProjetErpResponse findById(Long id) {
        ProjetErp projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet", id));
        return toResponse(projet);
    }

    /**
     * Crée un nouveau projet ERP.
     * Règle métier : le code produit doit être unique.
     */
    public ProjetErpResponse create(ProjetErpRequest request) {
        // Vérification de la règle métier
        if (projetRepository.existsByCodeProduit(request.getCodeProduit())) {
            throw new BusinessException(
                "Le code produit '" + request.getCodeProduit() + "' est déjà utilisé."
            );
        }

        // Construction de l'entité via le Builder de Lombok
        ProjetErp projet = ProjetErp.builder()
                .nom(request.getNom())
                .description(request.getDescription())
                .codeProduit(request.getCodeProduit())
                .build();

        // Sauvegarde en base → retourne l'entité avec l'id généré
        ProjetErp saved = projetRepository.save(projet);
        return toResponse(saved);
    }

    /**
     * Met à jour un projet existant.
     */
    public ProjetErpResponse update(Long id, ProjetErpRequest request) {
        ProjetErp projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet", id));

        // Vérifie l'unicité du code produit uniquement s'il change
        if (!projet.getCodeProduit().equals(request.getCodeProduit())
                && projetRepository.existsByCodeProduit(request.getCodeProduit())) {
            throw new BusinessException(
                "Le code produit '" + request.getCodeProduit() + "' est déjà utilisé."
            );
        }

        // Mise à jour des champs
        projet.setNom(request.getNom());
        projet.setDescription(request.getDescription());
        projet.setCodeProduit(request.getCodeProduit());

        // Pas besoin d'appeler save() explicitement ici :
        // @Transactional détecte automatiquement les changements sur l'entité (dirty checking)
        return toResponse(projet);
    }

    /**
     * Supprime un projet et en cascade tous ses modules, versions et clients.
     */
    public void delete(Long id) {
        if (!projetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projet", id);
        }
        projetRepository.deleteById(id);
    }

    /**
     * Convertit une entité ProjetErp en DTO ProjetErpResponse.
     * Méthode privée utilitaire — utilisée uniquement dans ce service.
     *
     * On appelle cette opération "mapper" ou "toResponse".
     * En production on utiliserait une librairie comme MapStruct,
     * mais on le fait manuellement pour comprendre le principe.
     */
    private ProjetErpResponse toResponse(ProjetErp projet) {
        return ProjetErpResponse.builder()
                .id(projet.getId())
                .nom(projet.getNom())
                .description(projet.getDescription())
                .codeProduit(projet.getCodeProduit())
                .build();
    }
}
