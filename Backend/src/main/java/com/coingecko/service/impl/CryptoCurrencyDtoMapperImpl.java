package com.coingecko.service.impl;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.dto.CryptoPriceDTO;
import com.coingecko.model.Cryptocurrency;
import com.coingecko.model.CryptoCurrencyPrice;
import com.coingecko.service.DtoMapper;
import org.springframework.stereotype.Component;

/**
 * Implementação: CryptoCurrencyDtoMapperImpl
 * Padrão: Strategy Pattern (pode ter múltiplas implementações)
 * Responsabilidade: Converter entre Cryptocurrency e CryptoCurrencyDTO
 */
@Component
public class CryptoCurrencyDtoMapperImpl implements DtoMapper {
    
    @Override
    public CryptoCurrencyDTO toDTO(Cryptocurrency cryptocurrency) {
        CryptoCurrencyDTO dto = new CryptoCurrencyDTO();
        dto.setId(cryptocurrency.getId());
        dto.setSymbol(cryptocurrency.getSymbol());
        dto.setName(cryptocurrency.getName());
        dto.setDescription(cryptocurrency.getDescription());
        dto.setImageUrl(cryptocurrency.getImageUrl());
        dto.setMarketCapRank(cryptocurrency.getMarketCapRank());
        dto.setLastUpdated(cryptocurrency.getLastUpdated());
        
        if (cryptocurrency.getCurrentPrice() != null) {
            dto.setPrice(mapPriceToDTO(cryptocurrency.getCurrentPrice()));
        }
        
        return dto;
    }
    
    @Override
    public Cryptocurrency toEntity(CryptoCurrencyDTO dto) {
        Cryptocurrency crypto = new Cryptocurrency();
        crypto.setId(dto.getId());
        crypto.setSymbol(dto.getSymbol());
        crypto.setName(dto.getName());
        crypto.setDescription(dto.getDescription());
        crypto.setImageUrl(dto.getImageUrl());
        crypto.setMarketCapRank(dto.getMarketCapRank());
        crypto.setLastUpdated(dto.getLastUpdated());
        
        return crypto;
    }
    
    private CryptoPriceDTO mapPriceToDTO(CryptoCurrencyPrice price) {
        return new CryptoPriceDTO(
            price.getPriceUsd(),
            price.getPriceEur(),
            price.getPriceBrl(),
            price.getVolume24hUsd(),
            price.getPriceChangePercentage24h(),
            price.getMarketCap(),
            price.getTimestamp()
        );
    }
}
