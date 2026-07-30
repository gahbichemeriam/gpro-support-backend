package com.gprosupport.backend.common.exception;

import com.gprosupport.backend.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour toute l'API.
 *
 * @RestControllerAdvice → intercepte les exceptions de TOUS les controllers.
 * Au lieu de gérer les erreurs dans chaque controller, on centralise ici.
 *
 * Avantage : format de réponse d'erreur uniforme dans toute l'API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les ressources introuvables → HTTP 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Gère les violations de règles métier → HTTP 400.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Gère les erreurs de validation des DTOs (@Valid, @NotBlank, @Size...) → HTTP 400.
     * Retourne un objet avec le détail de chaque champ invalide.
     *
     * Exemple de réponse :
     * {
     *   "success": false,
     *   "message": "Données invalides",
     *   "data": { "nom": "Le nom est obligatoire", "codeProduit": "Ne doit pas être vide" }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> erreurs = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String champ = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            erreurs.put(champ, message);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Données invalides : vérifiez les champs"));
    }

    /**
     * Filet de sécurité : toute exception non prévue → HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur interne du serveur."));
    }
}
