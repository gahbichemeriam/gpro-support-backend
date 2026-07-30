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
    Optional<ProblemeFonctionnalite> findByCodeErreur(String codeErreur);

    /** Tous les problèmes d'un module. */
    List<ProblemeFonctionnalite> findByModuleErpId(Long moduleId);

    /**
     * Recherche textuelle : titre contenant le mot-clé (insensible à la casse).
     * Spring génère : WHERE LOWER(titre) LIKE LOWER('%mot%')
     */
    List<ProblemeFonctionnalite> findByTitreContainingIgnoreCase(String mot);

    /**
     * Recherche combinée texte OU code erreur — pour la barre de recherche de l'agent.
     * @Query = on écrit le JPQL (Java Persistence Query Language) nous-mêmes.
     * JPQL ressemble à SQL mais utilise les noms de classes Java, pas les tables SQL.
     */
    @Query("SELECT p FROM ProblemeFonctionnalite p " +
           "WHERE LOWER(p.titre) LIKE LOWER(CONCAT('%', :terme, '%')) " +
           "OR LOWER(p.codeErreur) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<ProblemeFonctionnalite> rechercher(@Param("terme") String terme);
}
