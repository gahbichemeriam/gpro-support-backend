package com.gprosupport.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration Spring Security.
 *
 * PHASE ACTUELLE (développement) : toutes les routes sont ouvertes.
 * Cela permet de tester les APIs avec Postman sans token JWT.
 *
 * TODO - Semaine 2 : Activer JWT et restreindre les accès par rôle.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Définit les règles de sécurité HTTP.
     *
     * csrf().disable() → désactive la protection CSRF.
     *   Pourquoi ? CSRF est une protection pour les formulaires HTML classiques.
     *   Pour une API REST consommée par Angular avec JWT, c'est inutile et bloquant.
     *
     * authorizeHttpRequests → toutes les requêtes sont autorisées pour l'instant.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()   // TODO : restreindre en semaine 2
            );
        return http.build();
    }

    /**
     * Bean BCrypt pour hasher les mots de passe.
     * BCrypt est l'algorithme de hashage recommandé pour les mots de passe :
     * - Il est lent volontairement (difficile à forcer par brute force)
     * - Il intègre un "sel" aléatoire (deux mots de passe identiques donnent des hashs différents)
     *
     * Usage : passwordEncoder.encode("monMotDePasse") → "$2a$10$xyz..."
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
