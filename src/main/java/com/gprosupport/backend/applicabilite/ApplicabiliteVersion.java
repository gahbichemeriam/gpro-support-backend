package com.gprosupport.backend.applicabilite;

import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import com.gprosupport.backend.version.VersionErp;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant la matrice de compatibilité Bug ↔ Version.
 * Répond à la question : "Ce bug existe-t-il en v1.8 ? Quand a-t-il été corrigé ?"
 *
 * Exemple :
 *   probleme = ERR-PROD-201
 *   version  = v1.8.2
 *   statut   = PRESENT
 *   versionCorrective = v2.0.1  ← le bug est corrigé dans cette version
 *
 * Correspond à la table "applicabilite_version" en base de données.
 */
@Entity
@Table(name = "applicabilite_version",
       uniqueConstraints = @UniqueConstraint(columnNames = {"probleme_id", "version_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicabiliteVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Le problème concerné par cette entrée de matrice.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "probleme_id", nullable = false)
    private ProblemeFonctionnalite probleme;

    /**
     * La version ERP concernée.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private VersionErp version;

    /**
     * Statut du problème sur cette version (PRESENT, CORRIGE, NON_TESTE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_probleme", nullable = false, length = 20)
    @Builder.Default
    private StatutApplicabilite statutProbleme = StatutApplicabilite.PRESENT;

    /**
     * Version dans laquelle le bug a été définitivement corrigé.
     * Peut être null si aucun correctif n'est encore disponible.
     *
     * C'est ce champ qui déclenche l'alerte :
     * si versionActive du client < versionCorrective → alerte mise à niveau !
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_corrective_id")
    private VersionErp versionCorrective;
}
