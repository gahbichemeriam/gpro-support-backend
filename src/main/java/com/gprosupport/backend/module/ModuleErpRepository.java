package com.gprosupport.backend.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleErpRepository extends JpaRepository<ModuleErp, Long> {

    /**
     * Récupère tous les modules d'un projet donné.
     * Utilisé dans le parcours guidé : quand l'agent choisit un projet,
     * on charge dynamiquement ses modules (filtres en cascade).
     * Spring génère : SELECT * FROM module_erp WHERE projet_id = ?
     */
    List<ModuleErp> findByProjetErpId(Long projetId);
}
