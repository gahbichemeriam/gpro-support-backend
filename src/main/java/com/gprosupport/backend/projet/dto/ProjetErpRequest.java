package com.gprosupport.backend.projet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la création et la modification d'un projet ERP.
 * Reçu dans le corps (body) des requêtes POST et PUT.
 *
 * Les annotations @NotBlank, @Size → Spring Validation vérifie automatiquement
 * ces contraintes avant même que le code du Service soit appelé.
 */
@Getter
@Setter
public class ProjetErpRequest {

    @NotBlank(message = "Le nom du projet est obligatoire.")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères.")
    private String nom;

    private String description;  // optionnel

    @NotBlank(message = "Le code produit est obligatoire.")
    @Size(max = 50, message = "Le code produit ne doit pas dépasser 50 caractères.")
    private String codeProduit;
}
