package com.gprosupport.backend.probleme.dto;

import com.gprosupport.backend.probleme.PrioriteProbleme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemeRequest {

    @NotNull(message = "L'identifiant du module est obligatoire.")
    private Long moduleId;

    @NotBlank(message = "Le titre est obligatoire.")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères.")
    private String titre;

    @NotBlank(message = "Le code erreur est obligatoire.")
    @Size(max = 50, message = "Le code erreur ne doit pas dépasser 50 caractères.")
    private String codeErreur;

    private PrioriteProbleme priorite = PrioriteProbleme.MOYENNE;
}
