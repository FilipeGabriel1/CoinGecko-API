package com.coingecko.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.dto.CryptoPriceDTO;
import com.coingecko.exception.ExternalApiException;
import com.coingecko.service.CoinGeckoApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementação: CoinGeckoApiServiceImpl
 * Padrão: Adapter Pattern - adapta CoinGecko API para nossa aplicação
 * Responsabilidade: Consumir dados da API do CoinGecko com tratamento de erros robusto
 * SOLID - Dependency Inversion: Implementa interface CoinGeckoApiService
 */
@Slf4j
@Service
public class CoinGeckoApiServiceImpl implements CoinGeckoApiService {
    
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";
    private static final String API_NAME = "CoinGecko";
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${coingecko.api.enabled:true}")
    private boolean apiEnabled;
    
    public CoinGeckoApiServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Optional<CryptoCurrencyDTO> getCryptocurrencyData(String cryptoId) {
        try {
            if (!isApiAvailable()) {
                log.warn("CoinGecko API está desabilitada");
                return Optional.empty();
            }
            
            log.debug("Obtendo dados de CoinGecko para: {}", cryptoId);
            
            String url = buildCoinGeckoUrl(cryptoId);
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null || response.isEmpty()) {
                log.warn("Resposta vazia de CoinGecko para: {}", cryptoId);
                return Optional.empty();
            }
            
            JsonNode rootNode = objectMapper.readTree(response);
            
            if (rootNode.has(cryptoId)) {
                log.debug("Parseando resposta para cripto: {}", cryptoId);
                return parseApiResponse(cryptoId, rootNode.get(cryptoId));
            }
            
            log.warn("Cripto não encontrada em resposta de CoinGecko: {}", cryptoId);
            return Optional.empty();
            
        } catch (RestClientException e) {
            log.error("Erro de conectividade com CoinGecko API para cripto: {}", cryptoId, e);
            throw new ExternalApiException(API_NAME, "Erro ao conectar à API", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao consumir CoinGecko API para cripto: {}", cryptoId, e);
            throw new ExternalApiException(API_NAME, "Erro ao processar resposta", e);
        }
    }

    @Override
    public Map<String, CryptoCurrencyDTO> getCryptocurrencyDataBulk(List<String> cryptoIds) {
        try {
            if (!isApiAvailable()) {
                log.warn("CoinGecko API está desabilitada");
                return Collections.emptyMap();
            }

            if (cryptoIds == null) {
                return Collections.emptyMap();
            }

            List<String> normalizedIds = cryptoIds.stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .distinct()
                    .limit(250)
                    .collect(Collectors.toList());

            if (normalizedIds.isEmpty()) {
                return Collections.emptyMap();
            }

            String url = buildCoinGeckoBulkUrl(normalizedIds);
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isEmpty()) {
                log.warn("Resposta vazia de CoinGecko (bulk) idsCount={}", normalizedIds.size());
                return Collections.emptyMap();
            }

            JsonNode rootNode = objectMapper.readTree(response);
            Map<String, CryptoCurrencyDTO> result = new HashMap<>();

            for (String id : normalizedIds) {
                if (rootNode.has(id)) {
                    parseApiResponse(id, rootNode.get(id)).ifPresent(dto -> result.put(id, dto));
                }
            }

            return result;

        } catch (RestClientException e) {
            log.error("Erro de conectividade com CoinGecko API (bulk)", e);
            throw new ExternalApiException(API_NAME, "Erro ao conectar à API", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao consumir CoinGecko API (bulk)", e);
            throw new ExternalApiException(API_NAME, "Erro ao processar resposta", e);
        }
    }

    @Override
    public List<CryptoCurrencyDTO> getTopMarketCryptos(int limit) {
        try {
            if (!isApiAvailable()) {
                log.warn("CoinGecko API está desabilitada");
                return Collections.emptyList();
            }

            int safeLimit = Math.max(1, Math.min(limit, 250));
            String url = buildMarketsTopUrl(safeLimit);
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isEmpty()) {
                log.warn("Resposta vazia de CoinGecko (markets/top) limit={}", safeLimit);
                return Collections.emptyList();
            }

            JsonNode rootNode = objectMapper.readTree(response);
            return parseMarketsArray(rootNode);

        } catch (RestClientException e) {
            log.error("Erro de conectividade com CoinGecko API (markets/top) limit={}", limit, e);
            throw new ExternalApiException(API_NAME, "Erro ao conectar à API", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao consumir CoinGecko API (markets/top) limit={}", limit, e);
            throw new ExternalApiException(API_NAME, "Erro ao processar resposta", e);
        }
    }

    @Override
    public List<CryptoCurrencyDTO> getMarketCryptosByIds(List<String> ids) {
        try {
            if (!isApiAvailable()) {
                log.warn("CoinGecko API está desabilitada");
                return Collections.emptyList();
            }

            if (ids == null) {
                return Collections.emptyList();
            }

            List<String> normalizedIds = ids.stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .distinct()
                    .collect(Collectors.toList());

            if (normalizedIds.isEmpty()) {
                return Collections.emptyList();
            }

            String url = buildMarketsByIdsUrl(normalizedIds);
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isEmpty()) {
                log.warn("Resposta vazia de CoinGecko (markets/ids) idsCount={}", normalizedIds.size());
                return Collections.emptyList();
            }

            JsonNode rootNode = objectMapper.readTree(response);
            return parseMarketsArray(rootNode);

        } catch (RestClientException e) {
            log.error("Erro de conectividade com CoinGecko API (markets/ids)", e);
            throw new ExternalApiException(API_NAME, "Erro ao conectar à API", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao consumir CoinGecko API (markets/ids)", e);
            throw new ExternalApiException(API_NAME, "Erro ao processar resposta", e);
        }
    }
    
    @Override
    public boolean isApiAvailable() {
        return apiEnabled;
    }
    
    private String buildCoinGeckoUrl(String cryptoId) {
        return String.format(
                "%s/simple/price?ids=%s&vs_currencies=usd,eur,brl&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true",
                COINGECKO_API_URL,
                cryptoId.toLowerCase()
        );
    }

    private String buildCoinGeckoBulkUrl(List<String> cryptoIds) {
        String joined = String.join(",", cryptoIds);
        return String.format(
                "%s/simple/price?ids=%s&vs_currencies=usd,eur,brl&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true",
                COINGECKO_API_URL,
                joined
        );
    }

    private String buildMarketsTopUrl(int limit) {
        return String.format(
                "%s/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=%d&page=1&sparkline=false",
                COINGECKO_API_URL,
                limit
        );
    }

    private String buildMarketsByIdsUrl(List<String> ids) {
        String joined = String.join(",", ids);
        return String.format(
                "%s/coins/markets?vs_currency=usd&ids=%s&order=market_cap_desc&per_page=%d&page=1&sparkline=false",
                COINGECKO_API_URL,
                joined,
                Math.min(ids.size(), 250)
        );
    }

    private List<CryptoCurrencyDTO> parseMarketsArray(JsonNode rootNode) {
        if (rootNode == null || !rootNode.isArray()) {
            log.warn("Resposta inesperada de CoinGecko (markets): não é array");
            return Collections.emptyList();
        }

        List<CryptoCurrencyDTO> result = new ArrayList<>();
        for (JsonNode node : rootNode) {
            CryptoCurrencyDTO dto = parseMarketNode(node);
            if (dto != null) {
                result.add(dto);
            }
        }
        return result;
    }

    private CryptoCurrencyDTO parseMarketNode(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (!node.hasNonNull("id") || !node.hasNonNull("symbol") || !node.hasNonNull("name")) {
            return null;
        }

        CryptoCurrencyDTO dto = new CryptoCurrencyDTO();
        dto.setId(node.get("id").asText());
        dto.setSymbol(node.get("symbol").asText().toUpperCase());
        dto.setName(node.get("name").asText());

        if (node.hasNonNull("image")) {
            dto.setImageUrl(node.get("image").asText());
        }
        if (node.hasNonNull("market_cap_rank")) {
            dto.setMarketCapRank(node.get("market_cap_rank").asLong());
        }
        dto.setLastUpdated(LocalDateTime.now());

        return dto;
    }
    
    private Optional<CryptoCurrencyDTO> parseApiResponse(String cryptoId, JsonNode data) {
        try {
            CryptoCurrencyDTO dto = new CryptoCurrencyDTO();
            dto.setId(cryptoId);
            dto.setLastUpdated(LocalDateTime.now());
            
            CryptoPriceDTO priceDTO = parsePriceData(data);
            dto.setPrice(priceDTO);
            
            log.debug("Cripto parseada com sucesso: {}", cryptoId);
            return Optional.of(dto);
            
        } catch (NumberFormatException e) {
            log.error("Erro ao converter valores numéricos da API para cripto: {}", cryptoId, e);
            throw new ExternalApiException(API_NAME, "Erro ao parsear preços", e);
        } catch (Exception e) {
            log.error("Erro ao parsear resposta da API para cripto: {}", cryptoId, e);
            throw new ExternalApiException(API_NAME, "Erro ao processar dados", e);
        }
    }
    
    private CryptoPriceDTO parsePriceData(JsonNode data) {
        CryptoPriceDTO priceDTO = new CryptoPriceDTO();
        
        priceDTO.setPriceUsd(safeGetBigDecimal(data, "usd"));
        priceDTO.setPriceEur(safeGetBigDecimal(data, "eur"));
        priceDTO.setPriceBrl(safeGetBigDecimal(data, "brl"));
        priceDTO.setMarketCap(safeGetBigDecimal(data, "usd_market_cap"));
        priceDTO.setVolume24hUsd(safeGetBigDecimal(data, "usd_24h_vol"));
        priceDTO.setPriceChangePercentage24h(safeGetBigDecimal(data, "usd_24h_change"));
        priceDTO.setTimestamp(LocalDateTime.now());
        
        return priceDTO;
    }
    
    private BigDecimal safeGetBigDecimal(JsonNode node, String field) {
        try {
            if (node.has(field) && !node.get(field).isNull()) {
                return new BigDecimal(node.get(field).asText());
            }
        } catch (NumberFormatException e) {
            log.warn("Erro ao converter campo '{}' para BigDecimal", field, e);
        }
        return null;
    }
}
