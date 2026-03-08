package com.example.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestao de Beneficios",
                version = "v1",
                description = "API REST para CRUD de beneficios e transferencia com regra transacional via EJB.",
                contact = @Contact(name = "Desafio Fullstack Integrado"),
                license = @License(name = "Uso educacional")
        )
)
public class OpenApiConfig {
}
