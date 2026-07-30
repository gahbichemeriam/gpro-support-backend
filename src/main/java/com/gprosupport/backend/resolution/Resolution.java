package com.gprosupport.backend.resolution;

import com.gprosupport.backend.piecejointe.PieceJointe;
import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant la procédure de résolution d'un problème.
 * (ex : script SQL de correction, étapes de paramétrage)
 * Correspond à la table "resolution" en base de données.
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

    /**
     * Problème que cette résolution corrige.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "probleme_id", nullable = false)
    private ProblemeFonctionnalite probleme;

    /**
     * Type de résolution (SQL, PARAMETRAGE, PATCH_CODE, PROCEDURE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_resolution", nullable = false, length = 20)
    private TypeResolution typeResolution;

    /**
     * Description détaillée des étapes à suivre.
     * Peut contenir un script SQL complet, des instructions de paramétrage, etc.
     */
    @Column(name = "description_etapes", columnDefinition = "TEXT")
    private String descriptionEtapes;

    /**
     * Indique si cette résolution a été validée par l'équipe QA.
     * false par défaut → doit être approuvée avant utilisation en production.
     */
    @Column(name = "validation_qa", nullable = false)
    @Builder.Default
    private Boolean validationQa = false;

    /**
     * Pièces jointes associées à cette résolution (scripts SQL, PDF, captures).
     */
    @OneToMany(mappedBy = "resolution", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PieceJointe> piecesJointes = new ArrayList<>();
}
