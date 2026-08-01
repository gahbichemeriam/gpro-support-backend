package com.gprosupport.backend.piecejointe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gprosupport.backend.resolution.Resolution;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant un fichier joint à une résolution.
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
     * @JsonIgnoreProperties → quand Jackson sérialise PieceJointe en JSON,
     * il ignore les champs de Resolution listés ici pour éviter la récursion infinie
     * et alléger la réponse.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolution_id", nullable = false)
    @JsonIgnoreProperties({"piecesJointes", "probleme", "hibernateLazyInitializer"})
    private Resolution resolution;

    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;

    @Column(name = "chemin_stockage", nullable = false, length = 500)
    private String cheminStockage;

    @Column(name = "type_mime", length = 100)
    private String typeMime;
}
