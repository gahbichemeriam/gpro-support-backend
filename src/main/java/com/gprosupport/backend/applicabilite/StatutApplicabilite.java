package com.gprosupport.backend.applicabilite;

/**
 * Statut d'un problème pour une version donnée de l'ERP.
 * Correspond à l'ENUM PostgreSQL "statut_applicabilite".
 *
 * Exemple d'utilisation :
 *   Bug ERR-PROD-201 sur v1.8 → PRESENT
 *   Bug ERR-PROD-201 sur v2.0 → CORRIGE
 *   Bug ERR-PROD-201 sur v1.9 → NON_TESTE
 */
public enum StatutApplicabilite {
    PRESENT,    // Le bug est confirmé sur cette version
    CORRIGE,    // Le bug est corrigé dans cette version
    NON_TESTE   // Compatibilité non encore vérifiée
}
