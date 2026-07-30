package com.gprosupport.backend.common.exception;

/**
 * Exception levée quand une règle métier est violée.
 * Correspond à une réponse HTTP 400 Bad Request.
 *
 * Exemple d'utilisation :
 *   if (projetRepository.existsByCodeProduit(request.getCodeProduit())) {
 *       throw new BusinessException("Ce code produit existe déjà.");
 *   }
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
