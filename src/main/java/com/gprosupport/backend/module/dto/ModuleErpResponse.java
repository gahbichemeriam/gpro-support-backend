package com.gprosupport.backend.module.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO pour la réponse API d'un Module ERP.
 * On inclut projetId et projetNom pour que le frontend
 * sache à quel projet appartient ce module sans faire une 2ème requête.
 */
@Getter
@Builder
public class ModuleErpResponse {

    private Long id;
    private String nom;
    private String description;
    private Long projetId;
    private String projetNom;
}
