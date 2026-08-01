package com.gprosupport.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsable de la création et validation des tokens JWT.
 *
 * JWT = JSON Web Token. Structure : Header.Payload.Signature
 * - Header   : algorithme de signature (HS256)
 * - Payload  : données (email, rôle, expiration)
 * - Signature : garantit que le token n'a pas été modifié
 */
@Service
public class JwtService {

    /**
     * Clé secrète lue depuis application.properties.
     * Doit faire au moins 32 caractères pour HS256.
     */
    @Value("${app.jwt.secret}")
    private String secret;

    /**
     * Durée de validité du token en millisecondes.
     * 86400000 ms = 24 heures.
     */
    @Value("${app.jwt.expiration}")
    private long expiration;

    /**
     * Génère un token JWT pour un utilisateur.
     * Le "subject" du token = l'email de l'utilisateur.
     */
    public String genererToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // On ajoute le rôle dans le token pour que le frontend puisse l'utiliser
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())           // email
                .issuedAt(new Date())                         // date de création
                .expiration(new Date(System.currentTimeMillis() + expiration)) // date d'expiration
                .signWith(getCleSecrete())                    // signature avec notre clé secrète
                .compact();
    }

    /**
     * Extrait l'email (subject) depuis un token.
     */
    public String extraireEmail(String token) {
        return extraireClaim(token, Claims::getSubject);
    }

    /**
     * Vérifie si un token est valide pour un utilisateur donné.
     * Deux conditions : email correspond + token non expiré.
     */
    public boolean estValide(String token, UserDetails userDetails) {
        final String email = extraireEmail(token);
        return email.equals(userDetails.getUsername()) && !estExpire(token);
    }

    // ============ Méthodes privées utilitaires ============

    private boolean estExpire(String token) {
        return extraireClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extraireClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extraireTousClaims(token);
        return resolver.apply(claims);
    }

    private Claims extraireTousClaims(String token) {
        return Jwts.parser()
                .verifyWith(getCleSecrete())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Convertit la clé secrète (String) en objet SecretKey utilisable par JJWT.
     */
    private SecretKey getCleSecrete() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
