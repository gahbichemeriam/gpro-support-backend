package com.gprosupport.backend.probleme;

import com.gprosupport.backend.applicabilite.ApplicabiliteVersion;
import com.gprosupport.backend.module.ModuleErp;
import com.gprosupport.backend.resolution.Resolution;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant un problème fonctionnel connu de l'ERP.
 * (ex : ERR-PROD-201 "Erreur de calcul du stock en temps réel")
 * Correspond à la table "probleme_fonctionnalite" en base de données.
 */
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

    /**
     * Module fonctionnel concerné par ce problème.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleErp moduleErp;

    /**
     * Titre descriptif du problème.
     */
    @Column(nullable = false, length = 255)
    private String titre;

    /**
     * Code erreur unique (ex : "ERR-PROD-201").
     * Sert de référence rapide pour les agents support.
     */
    @Column(name = "code_erreur", nullable = false, unique = true, length = 50)
    private String codeErreur;

    /**
     * Niveau de priorité du problème.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PrioriteProbleme priorite = PrioriteProbleme.MOYENNE;

    /**
     * Date de création automatique (gérée par JPA, pas par le code métier).
     * updatable = false → cette date ne change jamais après la création.
     */
    @Column(name = "date_creation", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dateCreation = LocalDateTime.now();

    /**
     * Résolutions associées à ce problème (peut y en avoir plusieurs).
     */
    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Resolution> resolutions = new ArrayList<>();

    /**
     * Matrice de compatibilité version ↔ problème.
     */
    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ApplicabiliteVersion> applicabilites = new ArrayList<>();
}
