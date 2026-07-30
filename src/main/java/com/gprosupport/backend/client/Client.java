package com.gprosupport.backend.client;

import com.gprosupport.backend.projet.ProjetErp;
import com.gprosupport.backend.version.VersionErp;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant un client du parc (ex : "Client Alpha").
 * Un client utilise un projet ERP dans une version précise.
 * Correspond à la table "client" en base de données.
 *
 * ATTENTION : Cette table n'était pas dans le V1__init.sql original.
 * Elle sera ajoutée via une migration Flyway V2.
 */
@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom du client (ex : "Société Alpha SARL").
     */
    @Column(nullable = false, length = 150)
    private String nom;

    /**
     * Email de contact du client.
     */
    @Column(length = 255)
    private String email;

    /**
     * Le projet ERP utilisé par ce client.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private ProjetErp projetErp;

    /**
     * La version active de l'ERP chez ce client.
     * C'est ce champ qui permet de savoir si le client est impacté par un bug
     * et si le correctif est disponible pour sa version.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_active_id", nullable = false)
    private VersionErp versionActive;
}
