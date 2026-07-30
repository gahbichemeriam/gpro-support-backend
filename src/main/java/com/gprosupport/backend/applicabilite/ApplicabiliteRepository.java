package com.gprosupport.backend.applicabilite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicabiliteRepository extends JpaRepository<ApplicabiliteVersion, Long> {

    /** Toutes les entrées de la matrice pour un problème donné. */
    List<ApplicabiliteVersion> findByProblemeId(Long problemeId);

    /** Toutes les entrées de la matrice pour une version donnée. */
    List<ApplicabiliteVersion> findByVersionId(Long versionId);

    /** Entrée unique problème + version (contrainte UNIQUE en base). */
    Optional<ApplicabiliteVersion> findByProblemeIdAndVersionId(Long problemeId, Long versionId);
}
