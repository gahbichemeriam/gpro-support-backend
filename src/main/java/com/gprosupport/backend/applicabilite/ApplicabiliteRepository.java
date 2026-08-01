package com.gprosupport.backend.applicabilite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicabiliteRepository extends JpaRepository<ApplicabiliteVersion, Long> {

    @Query("SELECT a FROM ApplicabiliteVersion a " +
           "JOIN FETCH a.probleme JOIN FETCH a.version v JOIN FETCH v.projetErp " +
           "WHERE a.probleme.id = :problemeId")
    List<ApplicabiliteVersion> findByProblemeId(@Param("problemeId") Long problemeId);

    @Query("SELECT a FROM ApplicabiliteVersion a " +
           "JOIN FETCH a.probleme JOIN FETCH a.version v JOIN FETCH v.projetErp " +
           "WHERE a.version.id = :versionId")
    List<ApplicabiliteVersion> findByVersionId(@Param("versionId") Long versionId);

    @Query("SELECT a FROM ApplicabiliteVersion a " +
           "JOIN FETCH a.probleme JOIN FETCH a.version " +
           "WHERE a.probleme.id = :problemeId AND a.version.id = :versionId")
    Optional<ApplicabiliteVersion> findByProblemeIdAndVersionId(
            @Param("problemeId") Long problemeId,
            @Param("versionId") Long versionId);

    /**
     * Trouve toutes les applicabilités d'un problème qui ont une version corrective définie.
     * Utilisé pour la logique d'alerte automatique.
     */
    @Query("SELECT a FROM ApplicabiliteVersion a " +
           "JOIN FETCH a.probleme JOIN FETCH a.version JOIN FETCH a.versionCorrective " +
           "WHERE a.probleme.id = :problemeId AND a.versionCorrective IS NOT NULL")
    List<ApplicabiliteVersion> findByProblemeIdWithCorrectif(@Param("problemeId") Long problemeId);
}
