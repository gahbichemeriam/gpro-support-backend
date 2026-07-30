package com.gprosupport.backend.piecejointe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceJointeRepository extends JpaRepository<PieceJointe, Long> {

    /** Toutes les pièces jointes d'une résolution. */
    List<PieceJointe> findByResolutionId(Long resolutionId);
}
