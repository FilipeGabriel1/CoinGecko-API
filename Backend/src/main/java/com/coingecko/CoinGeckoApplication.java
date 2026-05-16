package com.coingecko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe Principal: CoinGeckoApplication
 * Responsabilidade: Iniciar aplicação Spring Boot
 */
@SpringBootApplication
public class CoinGeckoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CoinGeckoApplication.class, args);
    }
}
