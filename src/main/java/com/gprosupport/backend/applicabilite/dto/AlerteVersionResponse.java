package com.gprosupport.backend.applicabilite.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO pour l'alerte de mise à niveau.
 *
 * Répond à : "Le client X en v1.8 est-il impacté par le bug ERR-PROD-201 ?"
 * Si oui, on indique la version corrective recommandée.
 */
@Getter
@Builder
public class AlerteVersionResponse {

    private Long clientId;
    private String clientNom;
    private String versionActiveCode;
    private String problemeCodeErreur;
    private String problemeTitre;
    private String versionCorrectiveCode;

    /** true = le client doit mettre à jour son ERP. */
    private boolean alerteMiseAJour;
    private String messageAlerte;
}
