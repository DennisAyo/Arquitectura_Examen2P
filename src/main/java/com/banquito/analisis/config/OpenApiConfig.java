package com.banquito.analisis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Ventanilla Bancaria API")
                .version("1.0.0")
                .description("Microservicio para manejo de efectivo en ventanillas bancarias. " +
                           "Permite a los cajeros iniciar turnos, procesar transacciones y finalizar turnos " +
                           "con control de denominaciones de billetes y alertas de diferencias.")
                .contact(new Contact()
                    .name("Equipo de Desarrollo - Banco Banquito")
                    .email("desarrollo@banquito.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}