package com.coingecko.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração: ApplicationConfig
 * Padrão: Singleton (Beans do Spring são singletons por padrão)
 * Padrão: Factory Pattern (Factory de beans)
 * Responsabilidade: Configurar beans reutilizáveis
 * SOLID - Single Responsibility: Apenas configura beans
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Factory para RestTemplate
     * Singleton criado uma vez e compartilhado
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(java.time.Duration.ofSeconds(10))
            .setReadTimeout(java.time.Duration.ofSeconds(10))
            .build();
    }
    
    /**
     * Factory para ObjectMapper
     * Singleton para serialização/desserialização JSON
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
}
