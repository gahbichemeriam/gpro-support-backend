package com.gprosupport.backend.probleme;

import com.gprosupport.backend.applicabilite.ApplicabiliteVersion;
import com.gprosupport.backend.module.ModuleErp;
import com.gprosupport.backend.resolution.Resolution;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "probleme_fonctionnalite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemeFonctionnalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleErp moduleErp;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(name = "code_erreur", nullable = false, unique = true, length = 50)
    private String codeErreur;

    /**
     * @JdbcTypeCode(SqlTypes.NAMED_ENUM) → Hibernate utilise le type ENUM natif PostgreSQL.
     * Nécessaire car PostgreSQL refuse d'insérer un VARCHAR dans une colonne de type ENUM.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PrioriteProbleme priorite = PrioriteProbleme.MOYENNE;

    @Column(name = "date_creation", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Resolution> resolutions = new ArrayList<>();

    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ApplicabiliteVersion> applicabilites = new ArrayList<>();
}
