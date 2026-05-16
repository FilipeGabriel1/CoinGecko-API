package com.coingecko.service;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.model.Cryptocurrency;

/**
 * Interface: DtoMapper
 * Padrão: Builder/Mapper Pattern
 * Responsabilidade: Mapear entre modelos e DTOs
 * SOLID - Single Responsibility: Apenas faz transformação de dados
 */
public interface DtoMapper {
    CryptoCurrencyDTO toDTO(Cryptocurrency cryptocurrency);
    Cryptocurrency toEntity(CryptoCurrencyDTO dto);
}
