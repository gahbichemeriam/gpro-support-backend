package com.gprosupport.backend.piecejointe;

import com.gprosupport.backend.resolution.Resolution;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant un fichier joint à une résolution.
 * (ex : script SQL, PDF de procédure, capture d'écran)
 * Correspond à la table "piece_jointe" en base de données.
 */
@Entity
@Table(name = "piece_jointe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PieceJointe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Résolution à laquelle ce fichier est attaché.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolution_id", nullable = false)
    private Resolution resolution;

    /**
     * Nom original du fichier tel qu'uploadé par l'utilisateur.
     * (ex : "correction_stock_v1.sql")
     */
    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;

    /**
     * Chemin de stockage sur le serveur ou dans MinIO.
     * (ex : "/uploads/resolutions/42/correction_stock_v1.sql")
     */
    @Column(name = "chemin_stockage", nullable = false, length = 500)
    private String cheminStockage;

    /**
     * Type MIME du fichier pour l'affichage correct dans le navigateur.
     * (ex : "application/pdf", "image/png", "text/plain")
     */
    @Column(name = "type_mime", length = 100)
    private String typeMime;
}
