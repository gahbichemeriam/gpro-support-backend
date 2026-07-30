package com.gprosupport.backend.version;

/**
 * Cycle de vie d'une version ERP.
 * Ces valeurs correspondent exactement à l'ENUM PostgreSQL "statut_version".
 */
public enum StatutVersion {
    DEVELOPPEMENT,  // Version en cours de développement
    STAGING,        // Version en test / recette
    PRODUCTION,     // Version déployée chez les clients
    OBSOLETE        // Version plus supportée
}
