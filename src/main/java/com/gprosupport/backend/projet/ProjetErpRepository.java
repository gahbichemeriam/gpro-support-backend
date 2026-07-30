package com.gprosupport.backend.projet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data JPA pour l'entité ProjetErp.
 * JpaRepository<ProjetErp, Long> → entité = ProjetErp, type de l'id = Long
 * Spring génère automatiquement : findAll, findById, save, deleteById, count...
 */
@Repository
public interface ProjetErpRepository extends JpaRepository<ProjetErp, Long> {

    /**
     * Recherche un projet par son code produit unique.
     * Spring génère : SELECT * FROM projet_erp WHERE code_produit = ?
     * La convention de nommage "findBy + NomDuChamp" suffit, pas besoin d'écrire le SQL.
     */
    Optional<ProjetErp> findByCodeProduit(String codeProduit);

    /**
     * Vérifie si un code produit existe déjà (pour éviter les doublons).
     * Spring génère : SELECT COUNT(*) > 0 FROM projet_erp WHERE code_produit = ?
     */
    boolean existsByCodeProduit(String codeProduit);
}
