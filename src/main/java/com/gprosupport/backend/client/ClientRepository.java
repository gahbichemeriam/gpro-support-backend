package com.gprosupport.backend.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /** Tous les clients d'un projet. */
    List<Client> findByProjetErpId(Long projetId);

    /**
     * Trouve les clients dont la version active est inférieure à la version corrective
     * d'un problème donné → pour déclencher les alertes de mise à niveau.
     *
     * Cette requête sera utilisée dans la logique d'alerte automatique.
     */
    @Query("SELECT c FROM Client c " +
           "JOIN ApplicabiliteVersion av ON av.versionCorrective.id IS NOT NULL " +
           "WHERE c.projetErp.id = :projetId " +
           "AND c.versionActive.id = :versionId")
    List<Client> findClientsParVersionActive(@Param("projetId") Long projetId,
                                             @Param("versionId") Long versionId);
}
