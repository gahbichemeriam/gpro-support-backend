package com.gprosupport.backend.applicabilite.dto;

import com.gprosupport.backend.applicabilite.StatutApplicabilite;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicabiliteResponse {

    private Long id;
    private Long problemeId;
    private String problemeTitre;
    private String problemeCodeErreur;
    private Long versionId;
    private String versionCode;
    private StatutApplicabilite statutProbleme;
    private Long versionCorrectiveId;
    private String versionCorrectiveCode;
}
