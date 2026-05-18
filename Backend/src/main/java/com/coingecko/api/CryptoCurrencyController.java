package com.coingecko.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coingecko.dto.CryptoCurrencyDTO;
import com.coingecko.exception.ResourceNotFoundException;
import com.coingecko.service.CryptoCurrencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller: CryptoCurrencyController
 * Responsabilidade: Expor endpoints REST para criptomoedas com documentação OpenAPI
 * SOLID - Dependency Injection: Recebe serviço via construtor
 * SOLID - Single Responsibility: Apenas gerencia requisições HTTP
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/cryptocurrencies")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Cryptocurrencies", description = "Endpoints para gerenciar criptomoedas")
public class CryptoCurrencyController {
    
    private final CryptoCurrencyService cryptoCurrencyService;
    
    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }
    
    @Operation(summary = "Buscar criptomoeda por ID", description = "Retorna uma criptomoeda pelo seu ID único")
    @GetMapping("/{id}")
    public ResponseEntity<CryptoCurrencyDTO> findById(@PathVariable @NotBlank String id) {
        log.info("Buscando cripto por ID: {}", id);
        return cryptoCurrencyService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Criptomoeda", "ID", id));
    }
    
    @Operation(summary = "Buscar criptomoeda por símbolo", description = "Retorna uma criptomoeda pelo seu símbolo (ex: BTC)")
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<CryptoCurrencyDTO> findBySymbol(@PathVariable @NotBlank String symbol) {
        log.info("Buscando cripto por símbolo: {}", symbol);
        return cryptoCurrencyService.findBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Criptomoeda", "símbolo", symbol));
    }
    
    @Operation(summary = "Listar todas as criptomoedas", description = "Retorna lista de todas as criptomoedas cadastradas")
    @GetMapping({"", "/"})
    public ResponseEntity<List<CryptoCurrencyDTO>> findAll() {
        log.info("Listando todas as criptomoedas");
        List<CryptoCurrencyDTO> cryptos = cryptoCurrencyService.findAll();
        return ResponseEntity.ok(cryptos);
    }
    
    @Operation(summary = "Buscar criptomoedas por símbolo parcial", description = "Busca criptomoedas contendo o símbolo fornecido")
    @GetMapping("/search")
    public ResponseEntity<List<CryptoCurrencyDTO>> search(@RequestParam @NotBlank String symbol) {
        log.info("Buscando criptos com símbolo: {}", symbol);
        List<CryptoCurrencyDTO> cryptos = cryptoCurrencyService.searchBySymbol(symbol);
        return ResponseEntity.ok(cryptos);
    }
    
    @Operation(summary = "Obter top 10 criptomoedas", description = "Retorna as 10 principais criptomoedas por market cap")
    @GetMapping("/top")
    public ResponseEntity<List<CryptoCurrencyDTO>> getTop() {
        log.info("Obtendo top 10 criptomoedas");
        List<CryptoCurrencyDTO> cryptos = cryptoCurrencyService.getTopCryptos();
        return ResponseEntity.ok(cryptos);
    }
    
    @Operation(summary = "Criar nova criptomoeda", description = "Cria uma nova criptomoeda no banco de dados")
    @PostMapping({"", "/"})
    public ResponseEntity<CryptoCurrencyDTO> create(@Valid @RequestBody CryptoCurrencyDTO cryptoDTO) {
        log.info("Criando nova criptomoeda: {}", cryptoDTO.getId());
        CryptoCurrencyDTO saved = cryptoCurrencyService.save(cryptoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
            summary = "Popular dados de exemplo",
            description = "Cria criptomoedas no banco (defaults ou via CoinGecko). Use 'top' para top N por market cap, ou 'ids' (csv) para uma lista customizada. Opcionalmente atualiza preços via CoinGecko."
    )
    @PostMapping("/seed")
    public ResponseEntity<List<CryptoCurrencyDTO>> seed(
            @RequestParam(name = "updateFromCoinGecko", defaultValue = "false") boolean updateFromCoinGecko,
            @RequestParam(name = "top", required = false) Integer top,
            @RequestParam(name = "ids", required = false) String ids) {

        if (ids != null && !ids.isBlank()) {
            List<String> idList = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toList());
            log.info("Seeding por ids (count={}, updateFromCoinGecko={})", idList.size(), updateFromCoinGecko);
            return ResponseEntity.ok(cryptoCurrencyService.seedByIds(idList, updateFromCoinGecko));
        }

        if (top != null) {
            log.info("Seeding top={} (updateFromCoinGecko={})", top, updateFromCoinGecko);
            return ResponseEntity.ok(cryptoCurrencyService.seedTop(top, updateFromCoinGecko));
        }

        log.info("Seeding defaults (updateFromCoinGecko={})", updateFromCoinGecko);
        List<CryptoCurrencyDTO> seeded = cryptoCurrencyService.seedDefaults(updateFromCoinGecko);
        return ResponseEntity.ok(seeded);
    }
    
    @Operation(summary = "Deletar criptomoeda", description = "Deleta uma criptomoeda pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotBlank String id) {
        log.info("Deletando cripto: {}", id);
        cryptoCurrencyService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Atualizar de CoinGecko", description = "Atualiza dados da criptomoeda a partir da API externa do CoinGecko")
    @PutMapping("/{id}/update")
    public ResponseEntity<Void> updateFromCoinGecko(@PathVariable @NotBlank String id) {
        log.info("Atualizando cripto de CoinGecko: {}", id);
        cryptoCurrencyService.updateFromCoinGecko(id);
        return ResponseEntity.ok().build();
    }
}
