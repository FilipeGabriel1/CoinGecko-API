package com.coingecko.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.exception.ResourceNotFoundException;
import com.coingecko.model.CryptoCurrencyPrice;
import com.coingecko.model.Cryptocurrency;
import com.coingecko.repository.CryptoCurrencyPriceRepository;
import com.coingecko.repository.CryptocurrencyRepository;
import com.coingecko.service.CoinGeckoApiService;
import com.coingecko.service.CryptoCurrencyService;
import com.coingecko.service.DtoMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementação: CryptoCurrencyServiceImpl
 * Padrão: Service Pattern - encapsula a lógica de negócio
 * Responsabilidade: Gerenciar operações com criptomoedas com cache e validações
 * SOLID - Dependency Injection: Recebe dependências via construtor
 * SOLID - Single Responsibility: Apenas gerencia criptomoedas
 */
@Slf4j
@Service
@Transactional
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    
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
    @Cacheable(value = "cryptos", key = "#id")
    public Optional<CryptoCurrencyDTO> findById(String id) {
        log.debug("Buscando cripto por ID: {}", id);
        return cryptoRepository.findById(id)
                .map(dtoMapper::toDTO);
    }
    
    @Override
    @Cacheable(value = "cryptosBySymbol", key = "#symbol")
    public Optional<CryptoCurrencyDTO> findBySymbol(String symbol) {
        log.debug("Buscando cripto por símbolo: {}", symbol);
        return cryptoRepository.findBySymbol(symbol)
                .map(dtoMapper::toDTO);
    }
    
    @Override
    @Cacheable(value = "allCryptos")
    public List<CryptoCurrencyDTO> findAll() {
        log.debug("Listando todas as criptomoedas");
        return cryptoRepository.findAll()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CryptoCurrencyDTO> searchBySymbol(String symbol) {
        log.debug("Buscando criptos com símbolo: {}", symbol);
        return cryptoRepository.findBySymbolContainingIgnoreCase(symbol)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Cacheable(value = "topCryptos")
    public List<CryptoCurrencyDTO> getTopCryptos() {
        log.debug("Obtendo top 10 criptomoedas");
        return cryptoRepository.findByOrderByMarketCapRankAsc()
                .stream()
                .limit(10)
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @CacheEvict(value = {"allCryptos", "topCryptos"}, allEntries = true)
    public CryptoCurrencyDTO save(CryptoCurrencyDTO cryptoDTO) {
        log.info("Salvando criptomoeda: {}", cryptoDTO.getId());
        
        Cryptocurrency crypto = dtoMapper.toEntity(cryptoDTO);
        crypto.setLastUpdated(LocalDateTime.now());
        
        Cryptocurrency saved = cryptoRepository.save(crypto);
        
        if (cryptoDTO.getPrice() != null) {
            saveCryptoPrice(saved, cryptoDTO);
        }
        
        return dtoMapper.toDTO(saved);
    }
    
    @Override
    @CacheEvict(value = {"cryptos", "cryptosBySymbol", "allCryptos", "topCryptos"}, allEntries = true)
    public void delete(String id) {
        log.info("Deletando criptomoeda: {}", id);
        if (!cryptoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Criptomoeda", "ID", id);
        }
        cryptoRepository.deleteById(id);
    }
    
    @Override
    @CacheEvict(value = {"cryptos", "cryptosBySymbol", "allCryptos", "topCryptos"}, allEntries = true)
    public void updateFromCoinGecko(String id) {
        log.info("Atualizando criptomoeda de CoinGecko: {}", id);
        
        Optional<CryptoCurrencyDTO> apiData = coinGeckoApiService.getCryptocurrencyData(id);
        
        if (apiData.isPresent()) {
            CryptoCurrencyDTO dto = apiData.get();
            Cryptocurrency existing = cryptoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Criptomoeda", "ID", id));
            
            existing.setLastUpdated(LocalDateTime.now());
            
            if (dto.getPrice() != null) {
                priceRepository.findByCryptocurrency_Id(id)
                        .ifPresentOrElse(
                                price -> updateCryptoPrice(price, dto),
                                () -> saveCryptoPriceFromDTO(existing, dto)
                        );
            }
            
            cryptoRepository.save(existing);
            log.info("Atualizado dados de cripto: {}", id);
        } else {
            log.warn("Dados não encontrados na API CoinGecko para: {}", id);
        }
    }

    @Override
    @CacheEvict(value = {"cryptos", "cryptosBySymbol", "allCryptos", "topCryptos"}, allEntries = true)
    public List<CryptoCurrencyDTO> seedTop(int limit, boolean updateFromCoinGecko) {
        int safeLimit = Math.max(1, Math.min(limit, 250));
        List<CryptoCurrencyDTO> marketDtos = coinGeckoApiService.getTopMarketCryptos(safeLimit);
        return seedFromMarketDtos(marketDtos, updateFromCoinGecko);
    }

    @Override
    @CacheEvict(value = {"cryptos", "cryptosBySymbol", "allCryptos", "topCryptos"}, allEntries = true)
    public List<CryptoCurrencyDTO> seedByIds(List<String> ids, boolean updateFromCoinGecko) {
        if (ids == null) {
            return Collections.emptyList();
        }
        List<CryptoCurrencyDTO> marketDtos = coinGeckoApiService.getMarketCryptosByIds(ids);
        return seedFromMarketDtos(marketDtos, updateFromCoinGecko);
    }

    @Override
    @CacheEvict(value = {"cryptos", "cryptosBySymbol", "allCryptos", "topCryptos"}, allEntries = true)
    public List<CryptoCurrencyDTO> seedDefaults(boolean updateFromCoinGecko) {
        List<CryptoCurrencyDTO> defaults = List.of(
                new CryptoCurrencyDTO("bitcoin", "BTC", "Bitcoin", "Criptomoeda mais conhecida do mercado.", null, 1L, null, null),
                new CryptoCurrencyDTO("ethereum", "ETH", "Ethereum", "Plataforma de contratos inteligentes.", null, 2L, null, null),
                new CryptoCurrencyDTO("solana", "SOL", "Solana", "Blockchain focada em performance.", null, 3L, null, null)
        );

        List<CryptoCurrencyDTO> seeded = new ArrayList<>();

        for (CryptoCurrencyDTO dto : defaults) {
            Cryptocurrency entity = cryptoRepository.findById(dto.getId())
                    .orElseGet(() -> {
                        Cryptocurrency created = dtoMapper.toEntity(dto);
                        created.setLastUpdated(LocalDateTime.now());
                        return cryptoRepository.save(created);
                    });

            if (updateFromCoinGecko) {
                try {
                    coinGeckoApiService.getCryptocurrencyData(entity.getId())
                            .ifPresent(apiDto -> {
                                if (apiDto.getPrice() != null) {
                                    priceRepository.findByCryptocurrency_Id(entity.getId())
                                            .ifPresentOrElse(
                                                    price -> updateCryptoPrice(price, apiDto),
                                                    () -> saveCryptoPriceFromDTO(entity, apiDto)
                                            );
                                }
                            });
                } catch (Exception e) {
                    log.warn("Seed: falha ao atualizar preços via CoinGecko para {}", entity.getId(), e);
                }
            }

            seeded.add(dtoMapper.toDTO(entity));
        }

        return seeded;
    }

    private List<CryptoCurrencyDTO> seedFromMarketDtos(List<CryptoCurrencyDTO> marketDtos, boolean updateFromCoinGecko) {
        if (marketDtos == null || marketDtos.isEmpty()) {
            return Collections.emptyList();
        }

        List<CryptoCurrencyDTO> seeded = new ArrayList<>();

        Map<String, CryptoCurrencyDTO> bulkPrices = Collections.emptyMap();
        if (updateFromCoinGecko) {
            try {
                List<String> ids = marketDtos.stream()
                        .map(CryptoCurrencyDTO::getId)
                        .filter(s -> s != null && !s.isBlank())
                        .collect(Collectors.toList());
                bulkPrices = coinGeckoApiService.getCryptocurrencyDataBulk(ids);
            } catch (Exception e) {
                log.warn("Seed: falha ao obter preços em lote via CoinGecko", e);
            }
        }

        for (CryptoCurrencyDTO dto : marketDtos) {
            if (dto == null || dto.getId() == null || dto.getId().isBlank()) {
                continue;
            }

            Cryptocurrency entity = cryptoRepository.findById(dto.getId())
                    .map(existing -> {
                        existing.setSymbol(dto.getSymbol());
                        existing.setName(dto.getName());
                        existing.setImageUrl(dto.getImageUrl());
                        existing.setMarketCapRank(dto.getMarketCapRank());
                        existing.setLastUpdated(LocalDateTime.now());
                        return existing;
                    })
                    .orElseGet(() -> {
                        Cryptocurrency created = dtoMapper.toEntity(dto);
                        created.setLastUpdated(LocalDateTime.now());
                        return created;
                    });

            Cryptocurrency saved = cryptoRepository.save(entity);

            if (updateFromCoinGecko) {
                CryptoCurrencyDTO apiDto = bulkPrices.get(saved.getId());
                if (apiDto != null && apiDto.getPrice() != null) {
                    upsertPriceFromDTO(saved, apiDto);
                }
            }

            CryptoCurrencyDTO out = cryptoRepository.findById(saved.getId())
                    .map(dtoMapper::toDTO)
                    .orElse(dtoMapper.toDTO(saved));
            seeded.add(out);
        }

        seeded.sort(Comparator.comparing(CryptoCurrencyDTO::getMarketCapRank, Comparator.nullsLast(Long::compareTo)));
        return seeded;
    }

    private void upsertPriceFromDTO(Cryptocurrency crypto, CryptoCurrencyDTO dto) {
        if (crypto == null || crypto.getId() == null || dto == null || dto.getPrice() == null) {
            return;
        }

        priceRepository.findByCryptocurrency_Id(crypto.getId())
                .ifPresentOrElse(
                        price -> updateCryptoPrice(price, dto),
                        () -> saveCryptoPriceFromDTO(crypto, dto)
                );
    }
    
    private void saveCryptoPrice(Cryptocurrency crypto, CryptoCurrencyDTO dto) {
        CryptoCurrencyPrice price = new CryptoCurrencyPrice();
        price.setCryptocurrency(crypto);
        price.setPriceUsd(defaultBigDecimal(dto.getPrice().getPriceUsd()));
        price.setPriceEur(defaultBigDecimal(dto.getPrice().getPriceEur()));
        price.setPriceBrl(defaultBigDecimal(dto.getPrice().getPriceBrl()));
        price.setVolume24hUsd(defaultBigDecimal(dto.getPrice().getVolume24hUsd()));
        price.setPriceChangePercentage24h(defaultBigDecimal(dto.getPrice().getPriceChangePercentage24h()));
        price.setMarketCap(defaultBigDecimal(dto.getPrice().getMarketCap()));
        price.setTimestamp(LocalDateTime.now());

        crypto.setCurrentPrice(price);
        priceRepository.save(price);
        log.debug("Preço salvo para cripto: {}", crypto.getId());
    }
    
    private void saveCryptoPriceFromDTO(Cryptocurrency crypto, CryptoCurrencyDTO dto) {
        CryptoCurrencyPrice price = new CryptoCurrencyPrice();
        price.setCryptocurrency(crypto);
        price.setPriceUsd(defaultBigDecimal(dto.getPrice().getPriceUsd()));
        price.setPriceEur(defaultBigDecimal(dto.getPrice().getPriceEur()));
        price.setPriceBrl(defaultBigDecimal(dto.getPrice().getPriceBrl()));
        price.setVolume24hUsd(defaultBigDecimal(dto.getPrice().getVolume24hUsd()));
        price.setPriceChangePercentage24h(defaultBigDecimal(dto.getPrice().getPriceChangePercentage24h()));
        price.setMarketCap(defaultBigDecimal(dto.getPrice().getMarketCap()));
        price.setTimestamp(LocalDateTime.now());

        crypto.setCurrentPrice(price);
        priceRepository.save(price);
        log.debug("Preço inicial salvo para cripto: {}", crypto.getId());
    }
    
    private void updateCryptoPrice(CryptoCurrencyPrice price, CryptoCurrencyDTO dto) {
        price.setPriceUsd(defaultBigDecimal(dto.getPrice().getPriceUsd()));
        price.setPriceEur(defaultBigDecimal(dto.getPrice().getPriceEur()));
        price.setPriceBrl(defaultBigDecimal(dto.getPrice().getPriceBrl()));
        price.setVolume24hUsd(defaultBigDecimal(dto.getPrice().getVolume24hUsd()));
        price.setPriceChangePercentage24h(defaultBigDecimal(dto.getPrice().getPriceChangePercentage24h()));
        price.setMarketCap(defaultBigDecimal(dto.getPrice().getMarketCap()));
        price.setTimestamp(LocalDateTime.now());

        if (price.getCryptocurrency() != null) {
            price.getCryptocurrency().setCurrentPrice(price);
        }
        priceRepository.save(price);
        log.debug("Preço atualizado para cripto: {}", price.getCryptocurrency().getId());
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
