package com.gprosupport.backend.piecejointe;

import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.resolution.Resolution;
import com.gprosupport.backend.resolution.ResolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PieceJointeService {

    private final PieceJointeRepository pieceJointeRepository;
    private final ResolutionRepository resolutionRepository;

    /**
     * Lit le dossier d'upload depuis application.properties.
     * @Value("${app.upload.dir}") injecte la valeur "uploads"
     */
    @Value("${app.upload.dir}")
    private String uploadDir;

    /** Liste toutes les pièces jointes d'une résolution. */
    @Transactional(readOnly = true)
    public List<PieceJointe> findByResolution(Long resolutionId) {
        return pieceJointeRepository.findByResolutionId(resolutionId);
    }

    /**
     * Upload d'un fichier et enregistrement en base.
     *
     * @param resolutionId  L'id de la résolution à laquelle on attache le fichier
     * @param fichier       Le fichier envoyé par le client (MultipartFile)
     * @return              La pièce jointe sauvegardée
     */
    public PieceJointe upload(Long resolutionId, MultipartFile fichier) {
        // 1. Vérifier que la résolution existe
        Resolution resolution = resolutionRepository.findById(resolutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Résolution", resolutionId));

        // 2. Vérifier que le fichier n'est pas vide
        if (fichier.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }

        // 3. Créer le dossier d'upload s'il n'existe pas
        Path dossierUpload = Paths.get(uploadDir, "resolutions", String.valueOf(resolutionId));
        try {
            Files.createDirectories(dossierUpload);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d'upload.", e);
        }

        // 4. Générer un nom de fichier unique pour éviter les collisions
        // UUID = identifiant universel unique (ex : "a3f8b2c1-...")
        String nomOriginal = fichier.getOriginalFilename();
        String extension = (nomOriginal != null && nomOriginal.contains("."))
                ? nomOriginal.substring(nomOriginal.lastIndexOf("."))
                : "";
        String nomFichierStocke = UUID.randomUUID() + extension;

        // 5. Copier le fichier sur le disque
        Path cheminFichier = dossierUpload.resolve(nomFichierStocke);
        try {
            Files.copy(fichier.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier.", e);
        }

        // 6. Enregistrer les métadonnées en base de données
        PieceJointe pieceJointe = PieceJointe.builder()
                .resolution(resolution)
                .nomFichier(nomOriginal)
                .cheminStockage(cheminFichier.toString())
                .typeMime(fichier.getContentType())
                .build();

        return pieceJointeRepository.save(pieceJointe);
    }

    /**
     * Téléchargement d'un fichier.
     * Retourne une Resource Spring que le Controller enverra dans la réponse HTTP.
     */
    @Transactional(readOnly = true)
    public Resource telecharger(Long id) {
        PieceJointe pieceJointe = pieceJointeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce jointe", id));

        try {
            Path fichier = Paths.get(pieceJointe.getCheminStockage());
            Resource resource = new UrlResource(fichier.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Fichier introuvable sur le serveur.");
            }
            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Chemin de fichier invalide.", e);
        }
    }

    /** Supprime une pièce jointe (fichier + entrée en base). */
    public void delete(Long id) {
        PieceJointe pieceJointe = pieceJointeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce jointe", id));

        // Supprimer le fichier physique
        try {
            Files.deleteIfExists(Paths.get(pieceJointe.getCheminStockage()));
        } catch (IOException e) {
            // On log mais on continue la suppression en base
            System.err.println("Avertissement : impossible de supprimer le fichier physique : " + e.getMessage());
        }

        pieceJointeRepository.deleteById(id);
    }
}
