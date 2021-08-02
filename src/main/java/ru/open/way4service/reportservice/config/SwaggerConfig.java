package ru.open.way4service.reportservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Open bank Way4Service Report service API doc")
                                .version("0.0.1")
                                .contact(
                                        new Contact()
                                                .email("viktor.kovtunenko@open.ru")
                                                .name("Kovtunenko Victor")
                                                )
                );
    }
}