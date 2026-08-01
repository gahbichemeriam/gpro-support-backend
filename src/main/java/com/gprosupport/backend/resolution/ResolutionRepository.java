package com.gprosupport.backend.resolution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

    /** Toutes les résolutions d'un problème avec le problème chargé (JOIN FETCH). */
    @Query("SELECT r FROM Resolution r JOIN FETCH r.probleme WHERE r.probleme.id = :problemeId")
    List<Resolution> findByProblemeId(@Param("problemeId") Long problemeId);

    /** Résolutions validées QA d'un problème. */
    @Query("SELECT r FROM Resolution r JOIN FETCH r.probleme " +
           "WHERE r.probleme.id = :problemeId AND r.validationQa = true")
    List<Resolution> findByProblemeIdAndValidationQaTrue(@Param("problemeId") Long problemeId);

    /** Toutes les résolutions avec leur problème chargé. */
    @Query("SELECT r FROM Resolution r JOIN FETCH r.probleme")
    List<Resolution> findAllWithProbleme();
}
