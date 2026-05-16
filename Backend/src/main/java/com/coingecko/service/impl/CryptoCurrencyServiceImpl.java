package com.coingecko.service.impl;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.model.Cryptocurrency;
import com.coingecko.model.CryptoCurrencyPrice;
import com.coingecko.repository.CryptocurrencyRepository;
import com.coingecko.repository.CryptoCurrencyPriceRepository;
import com.coingecko.service.CoinGeckoApiService;
import com.coingecko.service.CryptoCurrencyService;
import com.coingecko.service.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação: CryptoCurrencyServiceImpl
 * Padrão: Service Pattern - encapsula a lógica de negócio
 * Responsabilidade: Gerenciar operações com criptomoedas
 * SOLID - Dependency Injection: Recebe dependências via construtor
 * SOLID - Single Responsibility: Apenas gerencia criptomoedas
 */
@Service
@Transactional
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoCurrencyServiceImpl.class);
    
    private final CryptocurrencyRepository cryptoRepository;
    private final CryptoCurrencyPriceRepository priceRepository;
    private final CoinGeckoApiService coinGeckoApiService;
    private final DtoMapper dtoMapper;
    
    public CryptoCurrencyServiceImpl(
            CryptocurrencyRepository cryptoRepository,
            CryptoCurrencyPriceRepository priceRepository,
            CoinGeckoApiService coinGeckoApiService,
            DtoMapper dtoMapper) {
        this.cryptoRepository = cryptoRepository;
        this.priceRepository = priceRepository;
        this.coinGeckoApiService = coinGeckoApiService;
        this.dtoMapper = dtoMapper;
    }
    
    @Override
    public Optional<CryptoCurrencyDTO> findById(String id) {
        return cryptoRepository.findById(id)
            .map(dtoMapper::toDTO);
    }
    
    @Override
    public Optional<CryptoCurrencyDTO> findBySymbol(String symbol) {
        return cryptoRepository.findBySymbol(symbol)
            .map(dtoMapper::toDTO);
    }
    
    @Override
    public List<CryptoCurrencyDTO> findAll() {
        return cryptoRepository.findAll()
            .stream()
            .map(dtoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CryptoCurrencyDTO> searchBySymbol(String symbol) {
        return cryptoRepository.findBySymbolContainingIgnoreCase(symbol)
            .stream()
            .map(dtoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CryptoCurrencyDTO> getTopCryptos() {
        return cryptoRepository.findByOrderByMarketCapRankAsc()
            .stream()
            .limit(10)
            .map(dtoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public CryptoCurrencyDTO save(CryptoCurrencyDTO cryptoDTO) {
        Cryptocurrency crypto = dtoMapper.toEntity(cryptoDTO);
        crypto.setLastUpdated(LocalDateTime.now());
        
        Cryptocurrency saved = cryptoRepository.save(crypto);
        
        if (cryptoDTO.getPrice() != null) {
            saveCryptoPrice(saved, cryptoDTO);
        }
        
        return dtoMapper.toDTO(saved);
    }
    
    @Override
    public void delete(String id) {
        cryptoRepository.deleteById(id);
    }
    
    @Override
    public void updateFromCoinGecko(String id) {
        Optional<CryptoCurrencyDTO> apiData = coinGeckoApiService.getCryptocurrencyData(id);
        
        if (apiData.isPresent()) {
            CryptoCurrencyDTO dto = apiData.get();
            Optional<Cryptocurrency> existing = cryptoRepository.findById(id);
            
            if (existing.isPresent()) {
                Cryptocurrency crypto = existing.get();
                crypto.setLastUpdated(LocalDateTime.now());
                
                if (dto.getPrice() != null) {
                    priceRepository.findByCryptocurrency_Id(id)
                        .ifPresentOrElse(
                            price -> updateCryptoPrice(price, dto),
                            () -> saveCryptoPriceFromDTO(crypto, dto)
                        );
                }
                
                cryptoRepository.save(crypto);
                logger.info("Atualizado dados de cripto: {}", id);
            } else {
                CryptoCurrencyDTO saved = save(dto);
                logger.info("Cripto criada: {}", saved.getId());
            }
        }
    }
    
    private void saveCryptoPrice(Cryptocurrency crypto, CryptoCurrencyDTO dto) {
        CryptoCurrencyPrice price = new CryptoCurrencyPrice();
        price.setCryptocurrency(crypto);
        price.setPriceUsd(dto.getPrice().getPriceUsd());
        price.setPriceEur(dto.getPrice().getPriceEur());
        price.setPriceBrl(dto.getPrice().getPriceBrl());
        price.setVolume24hUsd(dto.getPrice().getVolume24hUsd());
        price.setPriceChangePercentage24h(dto.getPrice().getPriceChangePercentage24h());
        price.setMarketCap(dto.getPrice().getMarketCap());
        price.setTimestamp(LocalDateTime.now());
        
        priceRepository.save(price);
    }
    
    private void saveCryptoPriceFromDTO(Cryptocurrency crypto, CryptoCurrencyDTO dto) {
        CryptoCurrencyPrice price = new CryptoCurrencyPrice();
        price.setCryptocurrency(crypto);
        price.setPriceUsd(dto.getPrice().getPriceUsd());
        price.setPriceEur(dto.getPrice().getPriceEur());
        price.setPriceBrl(dto.getPrice().getPriceBrl());
        price.setVolume24hUsd(dto.getPrice().getVolume24hUsd());
        price.setPriceChangePercentage24h(dto.getPrice().getPriceChangePercentage24h());
        price.setMarketCap(dto.getPrice().getMarketCap());
        price.setTimestamp(LocalDateTime.now());
        
        priceRepository.save(price);
    }
    
    private void updateCryptoPrice(CryptoCurrencyPrice price, CryptoCurrencyDTO dto) {
        price.setPriceUsd(dto.getPrice().getPriceUsd());
        price.setPriceEur(dto.getPrice().getPriceEur());
        price.setPriceBrl(dto.getPrice().getPriceBrl());
        price.setVolume24hUsd(dto.getPrice().getVolume24hUsd());
        price.setPriceChangePercentage24h(dto.getPrice().getPriceChangePercentage24h());
        price.setMarketCap(dto.getPrice().getMarketCap());
        price.setTimestamp(LocalDateTime.now());
        
        priceRepository.save(price);
    }
}
