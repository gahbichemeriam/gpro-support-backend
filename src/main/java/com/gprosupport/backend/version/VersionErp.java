package com.gprosupport.backend.version;

import com.gprosupport.backend.projet.ProjetErp;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private ProjetErp projetErp;

    @Column(name = "code_version", nullable = false, length = 30)
    private String codeVersion;

    @Column(name = "date_release")
    private LocalDate dateRelease;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatutVersion statut = StatutVersion.DEVELOPPEMENT;
}
