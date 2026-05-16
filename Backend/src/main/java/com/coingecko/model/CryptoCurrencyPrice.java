package com.coingecko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade: CryptoCurrencyPrice
 * Responsabilidade única: Armazenar informações de preço de criptomoeda
 * SOLID - Single Responsibility: Apenas modela dados de preço
 */
@Entity
@Table(name = "cryptocurrency_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyPrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "cryptocurrency_id", nullable = false)
    private Cryptocurrency cryptocurrency;
    
    @Column(nullable = false)
    private BigDecimal priceUsd;
    
    @Column(nullable = false)
    private BigDecimal priceEur;
    
    @Column(nullable = false)
    private BigDecimal priceBrl;
    
    @Column(nullable = false)
    private BigDecimal volume24hUsd;
    
    @Column(nullable = false)
    private BigDecimal priceChangePercentage24h;
    
    @Column(nullable = false)
    private BigDecimal marketCap;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
