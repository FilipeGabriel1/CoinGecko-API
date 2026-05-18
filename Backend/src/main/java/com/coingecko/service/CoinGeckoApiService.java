package com.coingecko.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.coingecko.dto.CryptoCurrencyDTO;

/**
 * Interface: CoinGeckoApiService
 * Padrão: Adapter Pattern + Strategy Pattern
 * Responsabilidade: Definir contrato para consumir CoinGecko API
 * SOLID - Open/Closed: Prontidão para mudanças de provider de API
 */
public interface CoinGeckoApiService {
    Optional<CryptoCurrencyDTO> getCryptocurrencyData(String cryptoId);

    /**
     * Busca preços/dados (USD/EUR/BRL, volume, market cap, 24h change) em lote.
     * Usa o endpoint /simple/price com múltiplos ids em uma única requisição.
     */
    Map<String, CryptoCurrencyDTO> getCryptocurrencyDataBulk(List<String> cryptoIds);

    /**
     * Obtém lista das principais criptomoedas por market cap (metadados: id/symbol/name/rank/imagem).
     * Não inclui preços em múltiplas moedas; use {@link #getCryptocurrencyData(String)} para preços.
     */
    List<CryptoCurrencyDTO> getTopMarketCryptos(int limit);

    /**
     * Obtém metadados de criptomoedas para uma lista de IDs (ex.: bitcoin, ethereum).
     * Não inclui preços em múltiplas moedas; use {@link #getCryptocurrencyData(String)} para preços.
     */
    List<CryptoCurrencyDTO> getMarketCryptosByIds(List<String> ids);

    boolean isApiAvailable();
}
