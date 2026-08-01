package com.gprosupport.backend.applicabilite;

import com.gprosupport.backend.applicabilite.dto.AlerteVersionResponse;
import com.gprosupport.backend.applicabilite.dto.ApplicabiliteRequest;
import com.gprosupport.backend.applicabilite.dto.ApplicabiliteResponse;
import com.gprosupport.backend.client.Client;
import com.gprosupport.backend.client.ClientRepository;
import com.gprosupport.backend.common.exception.BusinessException;
import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import com.gprosupport.backend.probleme.ProblemeRepository;
import com.gprosupport.backend.version.VersionErp;
import com.gprosupport.backend.version.VersionErpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicabiliteService {

    private final ApplicabiliteRepository applicabiliteRepository;
    private final ProblemeRepository problemeRepository;
    private final VersionErpRepository versionRepository;
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<ApplicabiliteResponse> findByProbleme(Long problemeId) {
        return applicabiliteRepository.findByProblemeId(problemeId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicabiliteResponse> findByVersion(Long versionId) {
        return applicabiliteRepository.findByVersionId(versionId)
                .stream().map(this::toResponse).toList();
    }

    /**
     * Crée une entrée dans la matrice Bug ↔ Version.
     * Ex : "Le bug ERR-PROD-201 est PRESENT en v1.8, corrigé en v2.0"
     */
    public ApplicabiliteResponse create(ApplicabiliteRequest request) {
        ProblemeFonctionnalite probleme = problemeRepository.findById(request.getProblemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Problème", request.getProblemeId()));

        VersionErp version = versionRepository.findById(request.getVersionId())
                .orElseThrow(() -> new ResourceNotFoundException("Version", request.getVersionId()));

        // Vérifie l'unicité problème + version
        if (applicabiliteRepository.findByProblemeIdAndVersionId(
                request.getProblemeId(), request.getVersionId()).isPresent()) {
            throw new BusinessException("Cette combinaison problème/version existe déjà.");
        }

        VersionErp versionCorrective = null;
        if (request.getVersionCorrectiveId() != null) {
            versionCorrective = versionRepository.findById(request.getVersionCorrectiveId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Version corrective", request.getVersionCorrectiveId()));
        }

        ApplicabiliteVersion applicabilite = ApplicabiliteVersion.builder()
                .probleme(probleme)
                .version(version)
                .statutProbleme(request.getStatutProbleme() != null
                        ? request.getStatutProbleme() : StatutApplicabilite.PRESENT)
                .versionCorrective(versionCorrective)
                .build();

        return toResponse(applicabiliteRepository.save(applicabilite));
    }

    /**
     * Logique d'alerte automatique — cœur du module versioning.
     *
     * Pour un problème donné, vérifie tous les clients du projet :
     * - Si le client est en version impactée ET sa version < version corrective
     *   → ALERTE : mettre à jour !
     *
     * @param problemeId L'id du problème à vérifier
     */
    @Transactional(readOnly = true)
    public List<AlerteVersionResponse> verifierAlertes(Long problemeId) {
        ProblemeFonctionnalite probleme = problemeRepository.findById(problemeId)
                .orElseThrow(() -> new ResourceNotFoundException("Problème", problemeId));

        // Récupère les entrées de la matrice avec une version corrective
        List<ApplicabiliteVersion> avecCorrectif =
                applicabiliteRepository.findByProblemeIdWithCorrectif(problemeId);

        List<AlerteVersionResponse> alertes = new ArrayList<>();

        for (ApplicabiliteVersion av : avecCorrectif) {
            Long projetId = av.getVersion().getProjetErp().getId();

            // Récupère tous les clients de ce projet
            List<Client> clients = clientRepository.findByProjetErpId(projetId);

            for (Client client : clients) {
                // Compare les ids de version : si version active = version impactée
                // C'est une simplification — en production on comparerait les numéros sémantiques
                boolean clientImpacte = client.getVersionActive().getId()
                        .equals(av.getVersion().getId());

                if (clientImpacte) {
                    alertes.add(AlerteVersionResponse.builder()
                            .clientId(client.getId())
                            .clientNom(client.getNom())
                            .versionActiveCode(client.getVersionActive().getCodeVersion())
                            .problemeCodeErreur(probleme.getCodeErreur())
                            .problemeTitre(probleme.getTitre())
                            .versionCorrectiveCode(av.getVersionCorrective().getCodeVersion())
                            .alerteMiseAJour(true)
                            .messageAlerte("⚠️ Le client " + client.getNom()
                                    + " utilise la version " + client.getVersionActive().getCodeVersion()
                                    + " qui contient le bug " + probleme.getCodeErreur()
                                    + ". Mise à jour vers " + av.getVersionCorrective().getCodeVersion()
                                    + " recommandée.")
                            .build());
                }
            }
        }

        return alertes;
    }

    public void delete(Long id) {
        if (!applicabiliteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Applicabilité", id);
        }
        applicabiliteRepository.deleteById(id);
    }

    private ApplicabiliteResponse toResponse(ApplicabiliteVersion a) {
        return ApplicabiliteResponse.builder()
                .id(a.getId())
                .problemeId(a.getProbleme().getId())
                .problemeTitre(a.getProbleme().getTitre())
                .problemeCodeErreur(a.getProbleme().getCodeErreur())
                .versionId(a.getVersion().getId())
                .versionCode(a.getVersion().getCodeVersion())
                .statutProbleme(a.getStatutProbleme())
                .versionCorrectiveId(a.getVersionCorrective() != null
                        ? a.getVersionCorrective().getId() : null)
                .versionCorrectiveCode(a.getVersionCorrective() != null
                        ? a.getVersionCorrective().getCodeVersion() : null)
                .build();
    }
}
