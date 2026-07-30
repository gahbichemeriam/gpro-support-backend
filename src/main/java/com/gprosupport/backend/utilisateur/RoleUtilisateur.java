package com.gprosupport.backend.utilisateur;

/**
 * Rôles des utilisateurs dans GPRO Support.
 * Correspond à l'ENUM PostgreSQL "role_utilisateur".
 *
 * Ces rôles déterminent les droits d'accès dans Spring Security.
 */
public enum RoleUtilisateur {
    AGENT_SUPPORT,  // Consulte et applique les résolutions
    RD,             // Reçoit les signalements, crée et valide les résolutions
    ADMIN           // Gestion complète (CRUD projets, modules, utilisateurs)
}
