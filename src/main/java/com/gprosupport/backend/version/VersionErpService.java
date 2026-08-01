package com.gprosupport.backend.version;

import com.gprosupport.backend.common.exception.BusinessException;
import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.projet.ProjetErp;
import com.gprosupport.backend.projet.ProjetErpRepository;
import com.gprosupport.backend.version.dto.VersionErpRequest;
import com.gprosupport.backend.version.dto.VersionErpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VersionErpService {

    private final VersionErpRepository versionRepository;
    private final ProjetErpRepository projetRepository;

    @Transactional(readOnly = true)
    public List<VersionErpResponse> findAll(Long projetId) {
        List<VersionErp> versions = (projetId != null)
                ? versionRepository.findByProjetErpId(projetId)
                : versionRepository.findAllWithProjet();
        return versions.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VersionErpResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public VersionErpResponse create(VersionErpRequest request) {
        ProjetErp projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet", request.getProjetId()));

        // Unicité : un projet ne peut pas avoir deux fois la même version
        if (versionRepository.existsByProjetErpIdAndCodeVersion(
                request.getProjetId(), request.getCodeVersion())) {
            throw new BusinessException("La version '" + request.getCodeVersion()
                    + "' existe déjà pour ce projet.");
        }

        VersionErp version = VersionErp.builder()
                .projetErp(projet)
                .codeVersion(request.getCodeVersion())
                .dateRelease(request.getDateRelease())
                .statut(request.getStatut() != null ? request.getStatut() : StatutVersion.DEVELOPPEMENT)
                .build();

        return toResponse(versionRepository.save(version));
    }

    public VersionErpResponse update(Long id, VersionErpRequest request) {
        VersionErp version = getOrThrow(id);

        // Vérifie unicité si le code change
        if (!version.getCodeVersion().equals(request.getCodeVersion())
                && versionRepository.existsByProjetErpIdAndCodeVersion(
                        version.getProjetErp().getId(), request.getCodeVersion())) {
            throw new BusinessException("La version '" + request.getCodeVersion()
                    + "' existe déjà pour ce projet.");
        }

        version.setCodeVersion(request.getCodeVersion());
        version.setDateRelease(request.getDateRelease());
        if (request.getStatut() != null) version.setStatut(request.getStatut());

        return toResponse(version);
    }

    public void delete(Long id) {
        if (!versionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Version", id);
        }
        versionRepository.deleteById(id);
    }

    private VersionErp getOrThrow(Long id) {
        return versionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Version", id));
    }

    private VersionErpResponse toResponse(VersionErp v) {
        return VersionErpResponse.builder()
                .id(v.getId())
                .codeVersion(v.getCodeVersion())
                .dateRelease(v.getDateRelease())
                .statut(v.getStatut())
                .projetId(v.getProjetErp().getId())
                .projetNom(v.getProjetErp().getNom())
                .build();
    }
}
