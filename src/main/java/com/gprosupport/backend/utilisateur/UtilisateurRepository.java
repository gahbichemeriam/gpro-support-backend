package com.gprosupport.backend.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    /**
     * Recherche un utilisateur par email.
     * Utilisé par Spring Security lors du login :
     * "Trouve-moi l'utilisateur avec cet email pour vérifier son mot de passe."
     */
    Optional<Utilisateur> findByEmail(String email);

    /** Vérifie si un email est déjà pris (éviter les doublons à l'inscription). */
    boolean existsByEmail(String email);
}
