package com.gprosupport.backend.version;

import com.gprosupport.backend.projet.ProjetErp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entité JPA représentant une version de l'ERP (ex : v1.8.2, v2.0.1).
 * Correspond à la table "version_erp" en base de données.
 */
@Entity
@Table(name = "version_erp",
       uniqueConstraints = @UniqueConstraint(columnNames = {"projet_id", "code_version"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VersionErp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relation ManyToOne : plusieurs versions appartiennent à un seul projet.
     * @JoinColumn définit le nom de la colonne de clé étrangère dans la table version_erp.
     * nullable = false → une version est toujours liée à un projet.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private ProjetErp projetErp;

    /**
     * Code de version (ex : "1.8.2", "2.0.1").
     */
    @Column(name = "code_version", nullable = false, length = 30)
    private String codeVersion;

    /**
     * Date de mise en production de cette version.
     */
    @Column(name = "date_release")
    private LocalDate dateRelease;

    /**
     * Statut du cycle de vie de cette version.
     * @Enumerated(STRING) → stocke "PRODUCTION" en base, pas "2" (l'index).
     * Toujours utiliser STRING, jamais ORDINAL (les index changent si on réordonne l'enum).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatutVersion statut = StatutVersion.DEVELOPPEMENT;
}
