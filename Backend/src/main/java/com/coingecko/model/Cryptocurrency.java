package com.coingecko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade: Cryptocurrency
 * Responsabilidade única: Representar uma criptomoeda no banco de dados
 * SOLID - Single Responsibility: Apenas modela dados de criptomoeda
 */
@Entity
@Table(name = "cryptocurrencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cryptocurrency {
    
    @Id
    private String id;
    
    @Column(nullable = false, unique = true)
    private String symbol;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cryptocurrency")
    private CryptoCurrencyPrice currentPrice;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    
    private Long marketCapRank;
}
