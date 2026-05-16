package com.coingecko.service;

import com.coingecko.dto.CryptoCurrencyDTO;
import java.util.Optional;

/**
 * Interface: CoinGeckoApiService
 * Padrão: Adapter Pattern + Strategy Pattern
 * Responsabilidade: Definir contrato para consumir CoinGecko API
 * SOLID - Open/Closed: Prontidão para mudanças de provider de API
 */
public interface CoinGeckoApiService {
    Optional<CryptoCurrencyDTO> getCryptocurrencyData(String cryptoId);
    boolean isApiAvailable();
}
