package com.gprosupport.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * Format de réponse standard pour toutes les API de GPRO Support.
 *
 * Toutes les réponses JSON auront cette structure :
 * {
 *   "success": true,
 *   "message": "Projet créé avec succès",
 *   "data": { ... }        ← présent seulement si success = true
 * }
 *
 * @JsonInclude(NON_NULL) → les champs null ne sont pas inclus dans le JSON
 * (ex : si data est null, il n'apparaît pas dans la réponse d'erreur)
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;

    /** Réponse de succès avec données. */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /** Réponse de succès sans données (ex : suppression). */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    /** Réponse d'erreur. */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
