package com.gprosupport.backend.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la création et modification d'un Module ERP.
 */
@Getter
@Setter
public class ModuleErpRequest {

    /**
     * L'id du projet auquel ce module appartient.
     * @NotNull → obligatoire : un module sans projet n'a pas de sens.
     */
    @NotNull(message = "L'identifiant du projet est obligatoire.")
    private Long projetId;

    @NotBlank(message = "Le nom du module est obligatoire.")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères.")
    private String nom;

    private String description;
}
