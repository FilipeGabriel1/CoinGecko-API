package com.coingecko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO: CryptoPriceDTO
 * Representa preços em múltiplas moedas
 * SOLID - Single Responsibility: Apenas transporta dados de preço
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceDTO {
    private BigDecimal priceUsd;
    private BigDecimal priceEur;
    private BigDecimal priceBrl;
    private BigDecimal volume24hUsd;
    private BigDecimal priceChangePercentage24h;
    private BigDecimal marketCap;
    private LocalDateTime timestamp;
}
