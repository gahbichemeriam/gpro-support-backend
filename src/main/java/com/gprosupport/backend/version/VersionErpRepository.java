package com.gprosupport.backend.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionErpRepository extends JpaRepository<VersionErp, Long> {

    /** Toutes les versions d'un projet. */
    List<VersionErp> findByProjetErpId(Long projetId);

    /** Recherche une version par son code exact dans un projet. */
    Optional<VersionErp> findByProjetErpIdAndCodeVersion(Long projetId, String codeVersion);

    /** Versions par statut (ex : toutes les versions PRODUCTION). */
    List<VersionErp> findByProjetErpIdAndStatut(Long projetId, StatutVersion statut);
}
