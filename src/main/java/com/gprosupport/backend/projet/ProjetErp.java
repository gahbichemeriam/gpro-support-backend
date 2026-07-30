package com.gprosupport.backend.projet;

import com.gprosupport.backend.module.ModuleErp;
import com.gprosupport.backend.version.VersionErp;
import com.gprosupport.backend.client.Client;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant un Projet ERP (ex : GPRO Industry SaaS).
 * Correspond à la table "projet_erp" en base de données.
 */
@Entity
@Table(name = "projet_erp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetErp {

    /**
     * Clé primaire auto-générée par PostgreSQL (BIGSERIAL).
     * IDENTITY = délègue la génération à la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom du projet (ex : "GPRO Industry SaaS").
     * nullable = false → obligatoire en base.
     * length = 150 → correspond au VARCHAR(150) du schéma SQL.
     */
    @Column(nullable = false, length = 150)
    private String nom;

    /**
     * Description libre du projet.
     * columnDefinition = "TEXT" → pas de limite de longueur.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Code produit unique (ex : "GPRO-IND-SAAS").
     * unique = true → contrainte d'unicité en base.
     */
    @Column(name = "code_produit", nullable = false, unique = true, length = 50)
    private String codeProduit;

    /**
     * Un projet contient plusieurs modules.
     * mappedBy = "projetErp" → c'est ModuleErp qui porte la clé étrangère.
     * cascade = ALL → si on supprime un projet, ses modules sont supprimés.
     * orphanRemoval = true → supprime les modules orphelins automatiquement.
     * fetch = LAZY → les modules ne sont chargés que si on les demande explicitement
     *                (évite de charger toute la BDD à chaque requête).
     */
    @OneToMany(mappedBy = "projetErp", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ModuleErp> modules = new ArrayList<>();

    /**
     * Un projet a plusieurs versions ERP.
     */
    @OneToMany(mappedBy = "projetErp", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<VersionErp> versions = new ArrayList<>();

    /**
     * Un projet peut avoir plusieurs clients associés.
     */
    @OneToMany(mappedBy = "projetErp", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Client> clients = new ArrayList<>();
}
