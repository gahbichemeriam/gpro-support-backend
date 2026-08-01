package com.gprosupport.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre JWT exécuté UNE FOIS par requête HTTP.
 *
 * Son rôle : intercepter chaque requête, lire le header "Authorization",
 * extraire et valider le token JWT, puis authentifier l'utilisateur
 * dans le contexte Spring Security.
 *
 * Flux d'une requête :
 *   HTTP Request
 *       ↓
 *   JwtAuthenticationFilter  (ce filtre)
 *       ↓ (si token valide)
 *   SecurityContext.setAuthentication(...)
 *       ↓
 *   Controller
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lire le header Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Si pas de token ou format incorrect → on laisse passer sans authentifier
        //    Le SecurityConfig décidera si la route est accessible sans auth
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token (enlever "Bearer ")
        final String jwt = authHeader.substring(7);

        // 4. Extraire l'email depuis le token
        final String email;
        try {
            email = jwtService.extraireEmail(jwt);
        } catch (Exception e) {
            // Token malformé ou signature invalide
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Si email extrait et utilisateur pas encore authentifié dans ce contexte
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Charger l'utilisateur depuis la base de données
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 7. Valider le token
            if (jwtService.estValide(jwt, userDetails)) {

                // 8. Créer l'objet d'authentification Spring Security
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,                           // credentials (pas besoin après validation JWT)
                        userDetails.getAuthorities()    // rôles
                    );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 9. Mettre l'authentification dans le contexte de la requête
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 10. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
