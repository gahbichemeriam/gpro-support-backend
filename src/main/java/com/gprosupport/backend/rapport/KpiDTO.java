package com.gprosupport.backend.rapport;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO pour les KPI du support.
 *
 * KPI = Key Performance Indicator (Indicateur Clé de Performance)
 *
 * - totalProblemes     : nombre total de problèmes dans la base
 * - problemesAvecResolution : problèmes qui ont au moins une résolution
 * - problemesSansResolution : problèmes sans résolution (à traiter en priorité)
 * - resolutionsValidees : résolutions approuvées par QA
 * - resolutionsEnAttente : résolutions non encore validées
 * - tauxResolution      : % de problèmes avec au moins une résolution
 * - tauxValidationQa    : % de résolutions validées
 * - totalClients        : nombre de clients dans le parc
 * - totalVersions       : nombre de versions ERP gérées
 */
@Getter
@Builder
public class KpiDTO {
    private long totalProblemes;
    private long problemesAvecResolution;
    private long problemesSansResolution;
    private long resolutionsValidees;
    private long resolutionsEnAttente;
    private double tauxResolution;
    private double tauxValidationQa;
    private long totalClients;
    private long totalVersions;
    private long totalProjets;
    private long totalModules;
}
