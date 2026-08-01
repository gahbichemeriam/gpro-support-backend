package com.gprosupport.backend.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {

    @NotBlank(message = "Le nom du client est obligatoire.")
    private String nom;

    @Email(message = "Format d'email invalide.")
    private String email;

    @NotNull(message = "L'identifiant du projet est obligatoire.")
    private Long projetId;

    @NotNull(message = "L'identifiant de la version active est obligatoire.")
    private Long versionActiveId;
}
