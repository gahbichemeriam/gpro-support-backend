package com.gprosupport.backend.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entité JPA représentant un utilisateur de l'application GPRO Support.
 * Correspond à la table "utilisateur" en base de données.
 *
 * Implémente UserDetails → Spring Security peut utiliser directement
 * cette entité pour l'authentification JWT (pas besoin d'une classe séparée).
 */
@Entity
@Table(name = "utilisateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    /**
     * Email = identifiant de connexion (login).
     */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Mot de passe hashé avec BCrypt (jamais en clair en base !).
     */
    @Column(name = "mot_de_passe_hash", nullable = false, length = 255)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleUtilisateur role;

    @Column(name = "date_creation", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dateCreation = LocalDateTime.now();

    // ============================================================
    // Méthodes de l'interface UserDetails (requises par Spring Security)
    // ============================================================

    /**
     * Retourne les autorisations de l'utilisateur.
     * Spring Security utilise le préfixe "ROLE_" par convention.
     * Ex : ROLE_ADMIN, ROLE_AGENT_SUPPORT
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Retourne le mot de passe (hashé) → utilisé par Spring Security.
     */
    @Override
    public String getPassword() {
        return motDePasseHash;
    }

    /**
     * Retourne le nom d'utilisateur → ici l'email.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Les 4 méthodes suivantes retournent true → compte toujours actif.
     * On pourra les enrichir plus tard (compte expiré, bloqué, etc.)
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
