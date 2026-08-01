package com.gprosupport.backend.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientResponse {

    private Long id;
    private String nom;
    private String email;
    private Long projetId;
    private String projetNom;
    private Long versionActiveId;
    private String versionActiveCode;
}
