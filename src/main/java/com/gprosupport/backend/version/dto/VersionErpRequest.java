package com.gprosupport.backend.version.dto;

import com.gprosupport.backend.version.StatutVersion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VersionErpRequest {

    @NotNull(message = "L'identifiant du projet est obligatoire.")
    private Long projetId;

    @NotBlank(message = "Le code version est obligatoire.")
    @Size(max = 30, message = "Le code version ne doit pas dépasser 30 caractères.")
    private String codeVersion;

    private LocalDate dateRelease;

    private StatutVersion statut = StatutVersion.DEVELOPPEMENT;
}
