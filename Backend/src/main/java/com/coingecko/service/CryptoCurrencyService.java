package com.coingecko.service;

import com.coingecko.dto.CryptoCurrencyDTO;
import java.util.List;
import java.util.Optional;

/**
 * Interface: CryptoCurrencyService
 * Padrão: Service Pattern + Dependency Inversion
 * Responsabilidade: Definir contrato para gerenciar criptomoedas
 * SOLID - Dependency Inversion: Depender da abstração, não da implementação
 */
public interface CryptoCurrencyService {
    Optional<CryptoCurrencyDTO> findById(String id);
    Optional<CryptoCurrencyDTO> findBySymbol(String symbol);
    List<CryptoCurrencyDTO> findAll();
    List<CryptoCurrencyDTO> searchBySymbol(String symbol);
    List<CryptoCurrencyDTO> getTopCryptos();
    CryptoCurrencyDTO save(CryptoCurrencyDTO crypto);
    void delete(String id);
    void updateFromCoinGecko(String id);
}
