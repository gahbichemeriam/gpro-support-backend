package com.gprosupport.backend.resolution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

    /** Toutes les résolutions d'un problème donné. */
    List<Resolution> findByProblemeId(Long problemeId);

    /** Résolutions validées par QA seulement. */
    List<Resolution> findByProblemeIdAndValidationQaTrue(Long problemeId);
}
