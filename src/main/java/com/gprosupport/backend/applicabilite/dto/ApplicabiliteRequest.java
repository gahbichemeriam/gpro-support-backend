package com.gprosupport.backend.applicabilite.dto;

import com.gprosupport.backend.applicabilite.StatutApplicabilite;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicabiliteRequest {

    @NotNull(message = "L'identifiant du problème est obligatoire.")
    private Long problemeId;

    @NotNull(message = "L'identifiant de la version est obligatoire.")
    private Long versionId;

    private StatutApplicabilite statutProbleme = StatutApplicabilite.PRESENT;

    /** Version qui corrige ce bug — null si pas encore de correctif. */
    private Long versionCorrectiveId;
}
