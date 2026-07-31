package com.gprosupport.backend.probleme.dto;

import com.gprosupport.backend.probleme.PrioriteProbleme;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProblemeResponse {

    private Long id;
    private String titre;
    private String codeErreur;
    private PrioriteProbleme priorite;
    private LocalDateTime dateCreation;
    private Long moduleId;
    private String moduleNom;
    private Long projetId;
    private String projetNom;
}
