package com.gprosupport.backend.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleErpRepository extends JpaRepository<ModuleErp, Long> {

    /**
     * Récupère tous les modules d'un projet avec le projet chargé (JOIN FETCH).
     * Évite LazyInitializationException quand on accède à module.getProjetErp().getNom()
     */
    @Query("SELECT m FROM ModuleErp m JOIN FETCH m.projetErp WHERE m.projetErp.id = :projetId")
    List<ModuleErp> findByProjetErpId(@Param("projetId") Long projetId);

    /** Tous les modules avec leur projet chargé. */
    @Query("SELECT m FROM ModuleErp m JOIN FETCH m.projetErp")
    List<ModuleErp> findAllWithProjet();
}
