package com.gprosupport.backend.common.exception;

/**
 * Exception levée quand une ressource n'est pas trouvée en base de données.
 * Correspond à une réponse HTTP 404 Not Found.
 *
 * Exemple d'utilisation dans un service :
 *   projetRepository.findById(id)
 *       .orElseThrow(() -> new ResourceNotFoundException("Projet", id));
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * @param ressource  Nom de l'entité (ex : "Projet", "Module")
     * @param id         L'identifiant qui n'a pas été trouvé
     */
    public ResourceNotFoundException(String ressource, Long id) {
        super(ressource + " avec l'id " + id + " introuvable.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
