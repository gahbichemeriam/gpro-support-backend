package com.gprosupport.backend.probleme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemeRepository extends JpaRepository<ProblemeFonctionnalite, Long> {

    /** Recherche par code erreur exact (ex : "ERR-PROD-201"). */
    @Query("SELECT p FROM ProblemeFonctionnalite p " +
           "JOIN FETCH p.moduleErp m " +
           "JOIN FETCH m.projetErp " +
           "WHERE p.codeErreur = :codeErreur")
    Optional<ProblemeFonctionnalite> findByCodeErreur(@Param("codeErreur") String codeErreur);

    /** Tous les problèmes avec leurs relations chargées. */
    @Query("SELECT p FROM ProblemeFonctionnalite p " +
           "JOIN FETCH p.moduleErp m " +
           "JOIN FETCH m.projetErp")
    List<ProblemeFonctionnalite> findAllWithRelations();

    /** Tous les problèmes d'un module avec leurs relations. */
    @Query("SELECT p FROM ProblemeFonctionnalite p " +
           "JOIN FETCH p.moduleErp m " +
           "JOIN FETCH m.projetErp " +
           "WHERE m.id = :moduleId")
    List<ProblemeFonctionnalite> findByModuleErpId(@Param("moduleId") Long moduleId);

    /** Recherche combinée texte OU code erreur avec relations chargées. */
    @Query("SELECT p FROM ProblemeFonctionnalite p " +
           "JOIN FETCH p.moduleErp m " +
           "JOIN FETCH m.projetErp " +
           "WHERE LOWER(p.titre) LIKE LOWER(CONCAT('%', :terme, '%')) " +
           "OR LOWER(p.codeErreur) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<ProblemeFonctionnalite> rechercher(@Param("terme") String terme);
}
