package com.coingecko.service;

import java.util.List;
import java.util.Optional;

import com.coingecko.dto.CryptoCurrencyDTO;

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

    /**
     * Cria/atualiza criptomoedas a partir do ranking (top N) da CoinGecko.
     * @param limit quantidade a popular (1..250)
     * @param updateFromCoinGecko se true, tenta atualizar preços via API CoinGecko após criar/garantir existência.
     */
    List<CryptoCurrencyDTO> seedTop(int limit, boolean updateFromCoinGecko);

    /**
     * Cria/atualiza criptomoedas a partir de uma lista de IDs (ex.: bitcoin, cardano, ripple).
     * @param ids lista de ids CoinGecko
     * @param updateFromCoinGecko se true, tenta atualizar preços via API CoinGecko após criar/garantir existência.
     */
    List<CryptoCurrencyDTO> seedByIds(List<String> ids, boolean updateFromCoinGecko);

    /**
     * Cria criptomoedas padrão (ex.: bitcoin/ethereum/solana) se ainda não existirem.
     * @param updateFromCoinGecko se true, tenta atualizar preços via API CoinGecko após criar/garantir existência.
     */
    List<CryptoCurrencyDTO> seedDefaults(boolean updateFromCoinGecko);
}
