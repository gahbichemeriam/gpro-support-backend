package com.gprosupport.backend.probleme;

/**
 * Niveau de priorité d'un problème fonctionnel.
 * Correspond à l'ENUM PostgreSQL "priorite_probleme".
 */
public enum PrioriteProbleme {
    BASSE,    // Problème mineur, pas bloquant
    MOYENNE,  // Problème gênant mais contournable
    HAUTE,    // Problème impactant la production
    CRITIQUE  // Bloquant — escalade immédiate R&D
}
