package com.gprosupport.backend.resolution;

import com.gprosupport.backend.piecejointe.PieceJointe;
import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant la procédure de résolution d'un problème.
 */
@Entity
@Table(name = "resolution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "probleme_id", nullable = false)
    private ProblemeFonctionnalite probleme;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type_resolution", nullable = false, length = 20)
    private TypeResolution typeResolution;

    @Column(name = "description_etapes", columnDefinition = "TEXT")
    private String descriptionEtapes;

    @Column(name = "validation_qa", nullable = false)
    @Builder.Default
    private Boolean validationQa = false;

    @OneToMany(mappedBy = "resolution", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PieceJointe> piecesJointes = new ArrayList<>();
}
