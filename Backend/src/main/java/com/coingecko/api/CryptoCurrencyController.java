package com.coingecko.api;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.service.CryptoCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller: CryptoCurrencyController
 * Responsabilidade: Expor endpoints REST para criptomoedas
 * SOLID - Dependency Injection: Recebe serviço via construtor
 * SOLID - Single Responsibility: Apenas gerencia requisições HTTP
 */
@RestController
@RequestMapping("/api/v1/cryptocurrencies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CryptoCurrencyController {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoCurrencyController.class);
    
    private final CryptoCurrencyService cryptoCurrencyService;
    
    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }
    
    /**
     * GET /api/v1/cryptocurrencies/{id}
     * Busca uma criptomoeda por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CryptoCurrencyDTO> findById(@PathVariable String id) {
        logger.info("Buscando cripto por ID: {}", id);
        Optional<CryptoCurrencyDTO> crypto = cryptoCurrencyService.findById(id);
        return crypto.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/v1/cryptocurrencies/symbol/{symbol}
     * Busca uma criptomoeda por símbolo
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<CryptoCurrencyDTO> findBySymbol(@PathVariable String symbol) {
        logger.info("Buscando cripto por símbolo: {}", symbol);
        Optional<CryptoCurrencyDTO> crypto = cryptoCurrencyService.findBySymbol(symbol);
        return crypto.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/v1/cryptocurrencies
     * Lista todas as criptomoedas
     */
    @GetMapping
    public ResponseEntity<List<CryptoCurrencyDTO>> findAll() {
        logger.info("Listando todas as criptomoedas");
        List<CryptoCurrencyDTO> cryptos = cryptoCurrencyService.findAll();
        return ResponseEntity.ok(cryptos);
    }
    
    /**
     * GET /api/v1/cryptocurrencies/search?symbol=BTC
     * Busca criptomoedas por símbolo (parcial)
     */
    @GetMapping("/search")
    public ResponseEntity<List<CryptoCurrencyDTO>> search(@RequestParam String symbol) {
        logger.info("Buscando criptos com símbolo: {}", symbol);
        List<CryptoCurrencyDTO> cryptos = cryptoCurrencyService.searchBySymbol(symbol);
        return ResponseEntity.ok(cryptos);
    }
    
    /**
     * GET /api/v1/cryptocurrencies/top
     * Retorna as top 10 criptomoedas por market cap
     */
    @GetMapping("/top")
    public ResponseEntity<List<CryptoCurrencyDTO>> getTop() {
        logger.info("Obtendo top 10 criptomoedas");
        List<CryptoCurrencyDTO> cryptos = cryptoCurrencyService.getTopCryptos();
        return ResponseEntity.ok(cryptos);
    }
    
    /**
     * POST /api/v1/cryptocurrencies
     * Cria uma nova criptomoeda
     */
    @PostMapping
    public ResponseEntity<CryptoCurrencyDTO> create(@RequestBody CryptoCurrencyDTO cryptoDTO) {
        logger.info("Criando nova criptomoeda: {}", cryptoDTO.getId());
        CryptoCurrencyDTO saved = cryptoCurrencyService.save(cryptoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    /**
     * DELETE /api/v1/cryptocurrencies/{id}
     * Deleta uma criptomoeda
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("Deletando cripto: {}", id);
        cryptoCurrencyService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * PUT /api/v1/cryptocurrencies/{id}/update
     * Atualiza dados da criptomoeda a partir da API do CoinGecko
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<Void> updateFromCoinGecko(@PathVariable String id) {
        logger.info("Atualizando cripto de CoinGecko: {}", id);
        cryptoCurrencyService.updateFromCoinGecko(id);
        return ResponseEntity.ok().build();
    }
}
