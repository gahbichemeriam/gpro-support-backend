package com.gprosupport.backend.resolution.dto;

import com.gprosupport.backend.resolution.TypeResolution;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResolutionResponse {

    private Long id;
    private TypeResolution typeResolution;
    private String descriptionEtapes;
    private Boolean validationQa;
    private Long problemeId;
    private String problemeTitre;
    private String problemeCodeErreur;
}
