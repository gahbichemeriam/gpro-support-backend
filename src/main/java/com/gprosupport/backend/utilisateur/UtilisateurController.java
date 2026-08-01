package com.gprosupport.backend.utilisateur;

import com.gprosupport.backend.common.dto.ApiResponse;
import com.gprosupport.backend.utilisateur.dto.LoginRequest;
import com.gprosupport.backend.utilisateur.dto.LoginResponse;
import com.gprosupport.backend.utilisateur.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    /**
     * POST /api/auth/register
     * Crée un compte et retourne un token JWT.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        LoginResponse response = utilisateurService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Compte créé avec succès.", response));
    }

    /**
     * POST /api/auth/login
     * Authentifie et retourne un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = utilisateurService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Connexion réussie.", response));
    }
}
