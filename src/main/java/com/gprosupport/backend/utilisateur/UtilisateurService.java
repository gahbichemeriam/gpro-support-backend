package com.gprosupport.backend.utilisateur;

import com.gprosupport.backend.common.exception.BusinessException;
import com.gprosupport.backend.security.JwtService;
import com.gprosupport.backend.utilisateur.dto.LoginRequest;
import com.gprosupport.backend.utilisateur.dto.LoginResponse;
import com.gprosupport.backend.utilisateur.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Crée un nouveau compte utilisateur.
     * Le mot de passe est hashé avec BCrypt avant stockage.
     */
    public LoginResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Cet email est déjà utilisé.");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .motDePasseHash(passwordEncoder.encode(request.getMotDePasse()))
                .role(request.getRole())
                .build();

        Utilisateur saved = utilisateurRepository.save(utilisateur);
        String token = jwtService.genererToken(saved);

        return LoginResponse.builder()
                .token(token)
                .email(saved.getEmail())
                .nom(saved.getNom())
                .role(saved.getRole())
                .build();
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     *
     * authenticationManager.authenticate() → Spring Security vérifie
     * automatiquement l'email + mot de passe via BCrypt.
     * Si invalide → lève BadCredentialsException → HTTP 403.
     */
    public LoginResponse login(LoginRequest request) {
        // Cette ligne vérifie email + mot de passe. Si incorrect → exception automatique
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));

        String token = jwtService.genererToken(utilisateur);

        return LoginResponse.builder()
                .token(token)
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .role(utilisateur.getRole())
                .build();
    }
}
