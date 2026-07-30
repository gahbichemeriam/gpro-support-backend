package com.gprosupport.backend.projet.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO pour la réponse API d'un projet ERP.
 * Envoyé dans le corps des réponses GET, POST, PUT.
 *
 * On ne renvoie que les champs nécessaires au frontend.
 * On n'inclut pas les listes (modules, versions) pour éviter
 * de surcharger la réponse — elles auront leurs propres endpoints.
 */
@Getter
@Builder
public class ProjetErpResponse {

    private Long id;
    private String nom;
    private String description;
    private String codeProduit;
}
