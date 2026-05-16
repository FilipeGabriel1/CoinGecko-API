package com.coingecko.service.impl;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.dto.CryptoPriceDTO;
import com.coingecko.service.CoinGeckoApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementação: CoinGeckoApiServiceImpl
 * Padrão: Adapter Pattern - adapta CoinGecko API para nossa aplicação
 * Responsabilidade: Consumir dados da API do CoinGecko
 * SOLID - Dependency Inversion: Implementa interface CoinGeckoApiService
 */
@Service
public class CoinGeckoApiServiceImpl implements CoinGeckoApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoApiServiceImpl.class);
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";
    
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
                logger.warn("CoinGecko API indisponível");
                return Optional.empty();
            }
            
            String url = String.format("%s/simple/price?ids=%s&vs_currencies=usd,eur,brl&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true", 
                COINGECKO_API_URL, cryptoId);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            
            if (rootNode.has(cryptoId)) {
                return parseApiResponse(cryptoId, rootNode.get(cryptoId));
            }
            
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Erro ao consumir CoinGecko API para cripto: {}", cryptoId, e);
            return Optional.empty();
        }
    }
    
    @Override
    public boolean isApiAvailable() {
        // Em produção, fazer uma chamada de health check
        return apiEnabled;
    }
    
    private Optional<CryptoCurrencyDTO> parseApiResponse(String cryptoId, JsonNode data) {
        try {
            CryptoCurrencyDTO dto = new CryptoCurrencyDTO();
            dto.setId(cryptoId);
            dto.setLastUpdated(LocalDateTime.now());
            
            CryptoPriceDTO priceDTO = new CryptoPriceDTO();
            priceDTO.setPriceUsd(new BigDecimal(data.get("usd").asText()));
            priceDTO.setPriceEur(new BigDecimal(data.get("eur").asText()));
            priceDTO.setPriceBrl(new BigDecimal(data.get("brl").asText()));
            
            if (data.has("usd_market_cap")) {
                priceDTO.setMarketCap(new BigDecimal(data.get("usd_market_cap").asText()));
            }
            
            if (data.has("usd_24h_vol")) {
                priceDTO.setVolume24hUsd(new BigDecimal(data.get("usd_24h_vol").asText()));
            }
            
            if (data.has("usd_24h_change")) {
                priceDTO.setPriceChangePercentage24h(new BigDecimal(data.get("usd_24h_change").asText()));
            }
            
            priceDTO.setTimestamp(LocalDateTime.now());
            dto.setPrice(priceDTO);
            
            return Optional.of(dto);
        } catch (Exception e) {
            logger.error("Erro ao parsear resposta da API para cripto: {}", cryptoId, e);
            return Optional.empty();
        }
    }
}
