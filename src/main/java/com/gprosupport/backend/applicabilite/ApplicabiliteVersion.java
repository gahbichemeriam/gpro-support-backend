package com.gprosupport.backend.applicabilite;

import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import com.gprosupport.backend.version.VersionErp;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entité JPA représentant la matrice de compatibilité Bug ↔ Version.
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "probleme_id", nullable = false)
    private ProblemeFonctionnalite probleme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private VersionErp version;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "statut_probleme", nullable = false, length = 20)
    @Builder.Default
    private StatutApplicabilite statutProbleme = StatutApplicabilite.PRESENT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_corrective_id")
    private VersionErp versionCorrective;
}
