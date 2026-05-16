package com.coingecko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO: CryptoCurrencyDTO
 * Responsabilidade: Transferir dados de criptomoeda para cliente
 * SOLID - Dependency Inversion: Depende de abstração, não de entidades concretas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyDTO {
    private String id;
    private String symbol;
    private String name;
    private String description;
    private String imageUrl;
    private Long marketCapRank;
    private CryptoPriceDTO price;
    private LocalDateTime lastUpdated;
}
