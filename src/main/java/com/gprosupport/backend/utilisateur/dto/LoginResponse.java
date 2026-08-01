package com.gprosupport.backend.utilisateur.dto;

import com.gprosupport.backend.utilisateur.RoleUtilisateur;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String token;
    private String email;
    private String nom;
    private RoleUtilisateur role;
}
