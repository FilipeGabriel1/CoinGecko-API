package com.coingecko.repository;

import com.coingecko.model.CryptoCurrencyPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository: CryptoCurrencyPriceRepository
 * Padrão: Repository Pattern
 * Responsabilidade: Acessar dados de preços
 */
@Repository
public interface CryptoCurrencyPriceRepository extends JpaRepository<CryptoCurrencyPrice, Long> {
    Optional<CryptoCurrencyPrice> findByCryptocurrency_Id(String cryptoId);
}
