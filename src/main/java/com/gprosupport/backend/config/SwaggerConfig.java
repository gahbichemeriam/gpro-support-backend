package com.gprosupport.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration Swagger / OpenAPI 3.
 *
 * Accessible sur : http://localhost:8081/swagger-ui/index.html
 *
 * Permet de :
 * - Visualiser toutes les APIs de GPRO Support
 * - Tester les endpoints directement depuis le navigateur
 * - Authentifier avec le token JWT via le bouton "Authorize"
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI gprosupportOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GPRO Support API")
                        .description("API REST du module d'aide au support ERP GPRO. " +
                                "Utilisez le bouton 'Authorize' pour entrer votre token JWT.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe GPRO Support")
                                .email("meriam.gahbiche@polytechnicien.tn")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Serveur de développement")
                ))
                // Configuration du schéma de sécurité JWT
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Entrez votre token JWT ici (sans le préfixe 'Bearer')")))
                // Appliquer la sécurité JWT à toutes les routes
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
