package com.coingecko.repository;

import com.coingecko.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository: CryptocurrencyRepository
 * Padrão: Repository Pattern - abstração de dados (SOLID - DIP)
 * Responsabilidade: Acessar dados de criptomoedas
 */
@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, String> {
    Optional<Cryptocurrency> findBySymbol(String symbol);
    List<Cryptocurrency> findBySymbolContainingIgnoreCase(String symbol);
    List<Cryptocurrency> findByOrderByMarketCapRankAsc();
}
