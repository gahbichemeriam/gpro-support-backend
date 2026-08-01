package com.gprosupport.backend.resolution;

import com.gprosupport.backend.common.exception.ResourceNotFoundException;
import com.gprosupport.backend.probleme.ProblemeFonctionnalite;
import com.gprosupport.backend.probleme.ProblemeRepository;
import com.gprosupport.backend.resolution.dto.ResolutionRequest;
import com.gprosupport.backend.resolution.dto.ResolutionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResolutionService {

    private final ResolutionRepository resolutionRepository;
    private final ProblemeRepository problemeRepository;

    @Transactional(readOnly = true)
    public List<ResolutionResponse> findAll(Long problemeId) {
        List<Resolution> resolutions = (problemeId != null)
                ? resolutionRepository.findByProblemeId(problemeId)
                : resolutionRepository.findAllWithProbleme();
        return resolutions.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResolutionResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public ResolutionResponse create(ResolutionRequest request) {
        ProblemeFonctionnalite probleme = problemeRepository.findById(request.getProblemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Problème", request.getProblemeId()));

        Resolution resolution = Resolution.builder()
                .probleme(probleme)
                .typeResolution(request.getTypeResolution())
                .descriptionEtapes(request.getDescriptionEtapes())
                .validationQa(request.getValidationQa() != null ? request.getValidationQa() : false)
                .build();

        return toResponse(resolutionRepository.save(resolution));
    }

    public ResolutionResponse update(Long id, ResolutionRequest request) {
        Resolution resolution = getOrThrow(id);

        if (!resolution.getProbleme().getId().equals(request.getProblemeId())) {
            ProblemeFonctionnalite probleme = problemeRepository.findById(request.getProblemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Problème", request.getProblemeId()));
            resolution.setProbleme(probleme);
        }

        resolution.setTypeResolution(request.getTypeResolution());
        resolution.setDescriptionEtapes(request.getDescriptionEtapes());
        if (request.getValidationQa() != null) resolution.setValidationQa(request.getValidationQa());

        return toResponse(resolution);
    }

    /**
     * Valide une résolution (passage validationQa = true).
     * Utilisé par l'équipe QA / R&D pour approuver une résolution.
     */
    public ResolutionResponse valider(Long id) {
        Resolution resolution = getOrThrow(id);
        resolution.setValidationQa(true);
        return toResponse(resolution);
    }

    public void delete(Long id) {
        if (!resolutionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Résolution", id);
        }
        resolutionRepository.deleteById(id);
    }

    private Resolution getOrThrow(Long id) {
        return resolutionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Résolution", id));
    }

    private ResolutionResponse toResponse(Resolution r) {
        return ResolutionResponse.builder()
                .id(r.getId())
                .typeResolution(r.getTypeResolution())
                .descriptionEtapes(r.getDescriptionEtapes())
                .validationQa(r.getValidationQa())
                .problemeId(r.getProbleme().getId())
                .problemeTitre(r.getProbleme().getTitre())
                .problemeCodeErreur(r.getProbleme().getCodeErreur())
                .build();
    }
}
