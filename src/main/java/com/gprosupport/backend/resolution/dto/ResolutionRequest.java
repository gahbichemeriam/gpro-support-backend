package com.gprosupport.backend.resolution.dto;

import com.gprosupport.backend.resolution.TypeResolution;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResolutionRequest {

    @NotNull(message = "L'identifiant du problème est obligatoire.")
    private Long problemeId;

    @NotNull(message = "Le type de résolution est obligatoire.")
    private TypeResolution typeResolution;

    private String descriptionEtapes;

    private Boolean validationQa = false;
}
