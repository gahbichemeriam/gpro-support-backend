package com.gprosupport.backend.resolution;

/**
 * Type de résolution apportée à un problème.
 * Correspond à l'ENUM PostgreSQL "type_resolution".
 */
public enum TypeResolution {
    SQL,          // Script SQL à exécuter directement en base
    PARAMETRAGE,  // Modification de paramètres dans l'ERP
    PATCH_CODE,   // Correctif de code à déployer
    PROCEDURE     // Procédure manuelle à suivre étape par étape
}
