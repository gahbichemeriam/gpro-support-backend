package com.gprosupport.backend.rapport;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO pour le rapport "Top Pannes".
 * Représente un problème avec son nombre de résolutions associées.
 */
@Getter
@AllArgsConstructor
public class TopPanneDTO {
    private Long problemeId;
    private String codeErreur;
    private String titre;
    private String moduleNom;
    private String priorite;
    private Long nbResolutions;
    private boolean qaValidee;
}
