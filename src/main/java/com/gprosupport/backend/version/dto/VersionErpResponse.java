package com.gprosupport.backend.version.dto;

import com.gprosupport.backend.version.StatutVersion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VersionErpResponse {

    private Long id;
    private String codeVersion;
    private LocalDate dateRelease;
    private StatutVersion statut;
    private Long projetId;
    private String projetNom;
}
