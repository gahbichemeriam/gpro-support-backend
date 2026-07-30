package com.gprosupport.backend.module;

import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import com.gprosupport.backend.projet.ProjetErp;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant un module fonctionnel d'un ERP.
 * (ex : Production, Stocks, Ventes, RH)
 * Correspond à la table "module_erp" en base de données.
 */
@Entity
@Table(name = "module_erp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleErp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Projet ERP auquel appartient ce module.
     * Un module ne peut pas exister sans projet (clé étrangère obligatoire).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private ProjetErp projetErp;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Un module contient plusieurs problèmes fonctionnels.
     */
    @OneToMany(mappedBy = "moduleErp", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProblemeFonctionnalite> problemes = new ArrayList<>();
}
