# 📐 Princípios SOLID Explicados

## O que é SOLID?

SOLID é um acrônimo para cinco princípios de design orientado a objetos que tornam o código mais compreensível, flexível e manutenível.

| Letra | Princípio | Foco |
|-------|-----------|------|
| **S** | Single Responsibility | Uma responsabilidade por classe |
| **O** | Open/Closed | Aberto para extensão, fechado para modificação |
| **L** | Liskov Substitution | Subtipos substituem supertipos |
| **I** | Interface Segregation | Interfaces específicas |
| **D** | Dependency Inversion | Depender de abstrações |

---

## 1. Single Responsibility Principle (SRP)

### ✅ Conceito
Cada classe deve ter uma e apenas uma razão para mudar.

### 🔴 Violação - ❌ ERRADO

```java
@Service
public class CryptoService {
    // ❌ Responsabilidade 1: Buscar dados
    public Cryptocurrency fetchFromCoinGecko(String id) { }
    
    // ❌ Responsabilidade 2: Persister no banco
    public void saveToDatabase(Cryptocurrency crypto) { }
    
    // ❌ Responsabilidade 3: Enviar email
    public void sendNotificationEmail(String email) { }
    
    // ❌ Responsabilidade 4: Fazer log
    public void logActivity(String message) { }
}
```

**Problemas**:
- Classe muito grande
- Difícil de testar
- Se email mudar, afeta toda a classe
- Múltiplas razões para mudar

### 🟢 Solução - ✅ CORRETO

```java
// 1️⃣ Responsabilidade: Buscar dados da API
@Service
public class CoinGeckoApiService {
    public Optional<CryptoCurrencyDTO> getCryptocurrencyData(String id) { }
}

// 2️⃣ Responsabilidade: Persistir no banco
@Service
public class CryptocurrencyPersistenceService {
    public void save(Cryptocurrency crypto) { }
}

// 3️⃣ Responsabilidade: Notificação
@Service
public class NotificationService {
    public void sendEmail(String email) { }
}

// 4️⃣ Responsabilidade: Logging
@Slf4j
@Service
public class CryptoCurrencyService {
    private final CoinGeckoApiService apiService;
    private final CryptocurrencyPersistenceService persistenceService;
    private final NotificationService notificationService;
    
    public void registerNewCrypto(String id) {
        logger.info("Registrando cripto: {}", id);
        
        CryptoCurrencyDTO dto = apiService.getCryptocurrencyData(id).orElse(null);
        persistenceService.save(dto);
        notificationService.sendEmail("Cripto registrada!");
    }
}
```

### Backend Exemplo ✅

```java
// ✅ CORRETO: Cada classe tem UMA responsabilidade

// Responsabilidade: Acessar dados
@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, String> {
    Optional<Cryptocurrency> findBySymbol(String symbol);
}

// Responsabilidade: Lógica de negócio
@Service
public class CryptoCurrencyServiceImpl {
    public List<CryptoCurrencyDTO> getTopCryptos() { }
}

// Responsabilidade: Expor API
@RestController
public class CryptoCurrencyController {
    @GetMapping("/top")
    public ResponseEntity<List<CryptoCurrencyDTO>> getTop() { }
}

// Responsabilidade: Transformar dados
@Component
public class CryptoCurrencyDtoMapperImpl implements DtoMapper {
    public CryptoCurrencyDTO toDTO(Cryptocurrency crypto) { }
}
```

### Frontend Exemplo ✅

```javascript
// ✅ CORRETO: Componentes pequenos com uma responsabilidade

// SearchBar: APENAS busca
function SearchBar() {
    const [searchTerm, setSearchTerm] = useState('');
    const handleSearch = async (term) => {
        const results = await searchCryptos(term);
        setSearchResults(results);
    };
    return <input onChange={handleSearch} />;
}

// CryptoCard: APENAS renderiza informações
function CryptoCard({ crypto }) {
    return (
        <div>
            <h3>{crypto.name}</h3>
            <p>${crypto.price.priceUsd}</p>
        </div>
    );
}

// CryptoList: APENAS lista
function CryptoList({ cryptos }) {
    return cryptos.map(c => <CryptoCard crypto={c} />);
}
```

---

## 2. Open/Closed Principle (OCP)

### ✅ Conceito
Classes devem ser **abertas para extensão** mas **fechadas para modificação**.

### 🔴 Violação - ❌ ERRADO

```java
@Service
public class CryptoDiscountService {
    public BigDecimal calculatePrice(Cryptocurrency crypto) {
        // ❌ Cliente solicitou novo tipo de desconto
        // Preciso modificar a classe (RUIM!)
        
        if ("bitcoin".equals(crypto.getId())) {
            return crypto.getPrice().multiply(new BigDecimal("0.9")); // 10% desconto
        } else if ("ethereum".equals(crypto.getId())) {
            return crypto.getPrice().multiply(new BigDecimal("0.95")); // 5% desconto
        } else if ("cardano".equals(crypto.getId())) {
            return crypto.getPrice(); // Sem desconto
        }
        return crypto.getPrice();
    }
}
```

**Problema**: Cada novo tipo precisa modificar a classe

### 🟢 Solução - ✅ CORRETO

```java
// Interface: Aberta para extensão
public interface DiscountStrategy {
    BigDecimal apply(BigDecimal price);
}

// Implementações: Extensões sem modificar original
public class BitcoinDiscount implements DiscountStrategy {
    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.multiply(new BigDecimal("0.9")); // 10%
    }
}

public class EthereumDiscount implements DiscountStrategy {
    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.multiply(new BigDecimal("0.95")); // 5%
    }
}

public class NoDiscount implements DiscountStrategy {
    @Override
    public BigDecimal apply(BigDecimal price) {
        return price;
    }
}

// Factory cria a estratégia apropriada
public class DiscountStrategyFactory {
    public DiscountStrategy getStrategy(String cryptoId) {
        return switch(cryptoId) {
            case "bitcoin" -> new BitcoinDiscount();
            case "ethereum" -> new EthereumDiscount();
            default -> new NoDiscount();
        };
    }
}

// Service usa a estratégia (NUNCA modifica)
@Service
public class CryptoDiscountService {
    private final DiscountStrategyFactory factory;
    
    public BigDecimal calculatePrice(Cryptocurrency crypto) {
        DiscountStrategy strategy = factory.getStrategy(crypto.getId());
        return strategy.apply(crypto.getPrice().getPriceUsd());
    }
}
```

### Implementação no Projeto ✅

```java
// Interface: Fechada para modificação
public interface CoinGeckoApiService {
    Optional<CryptoCurrencyDTO> getCryptocurrencyData(String cryptoId);
    boolean isApiAvailable();
}

// Implementação: Aberta para extensão nova
@Service
public class CoinGeckoApiServiceImpl implements CoinGeckoApiService {
    // Implementação específica
}

// No futuro, se precisar de outra API:
@Service
public class CoinMarketCapApiServiceImpl implements CoinGeckoApiService {
    // Nova implementação sem modificar código existente!
}
```

---

## 3. Liskov Substitution Principle (LSP)

### ✅ Conceito
Objetos de subclasses devem poder substituir objetos da superclasse sem quebrar o programa.

### 🔴 Violação - ❌ ERRADO

```java
// Classe base
public abstract class Cryptocurrency {
    public abstract BigDecimal getPrice();
}

// Subclasse que viola o contrato
public class SpecialCrypto extends Cryptocurrency {
    @Override
    public BigDecimal getPrice() {
        throw new UnsupportedOperationException("Este token não tem preço!");
    }
}

// Código que quebra
public void printPrice(Cryptocurrency crypto) {
    System.out.println(crypto.getPrice()); // ❌ Pode lançar exceção!
}
```

### 🟢 Solução - ✅ CORRETO

```java
// Interface bem definida
public interface CryptoCurrency {
    BigDecimal getPrice();
    String getName();
}

// Implementações respeitam o contrato
public class Bitcoin implements CryptoCurrency {
    @Override
    public BigDecimal getPrice() {
        return fetchPriceFromAPI();
    }
    
    @Override
    public String getName() {
        return "Bitcoin";
    }
}

public class Ethereum implements CryptoCurrency {
    @Override
    public BigDecimal getPrice() {
        return fetchPriceFromAPI();
    }
    
    @Override
    public String getName() {
        return "Ethereum";
    }
}

// Código confiável
public void printPrice(CryptoCurrency crypto) {
    System.out.println(crypto.getName() + ": " + crypto.getPrice());
    // ✅ Funciona com qualquer implementação!
}
```

### Frontend - React Exemplo ✅

```javascript
// Interface (contrato)
// Props: crypto (Object), onSelect (Function)

// ✅ Bitcoin compatível
function BitcoinCard({ crypto, onSelect }) {
    return <div onClick={() => onSelect(crypto)}>{crypto.name}</div>;
}

// ✅ Ethereum compatível (mesma interface)
function EthereumCard({ crypto, onSelect }) {
    return <div onClick={() => onSelect(crypto)}>{crypto.name}</div>;
}

// ✅ Componente genérico funciona com ambos
function CryptoList({ cryptos, onSelect }) {
    return cryptos.map(c => 
        <CryptoCard crypto={c} onSelect={onSelect} />
    );
}
```

---

## 4. Interface Segregation Principle (ISP)

### ✅ Conceito
Clientes não devem depender de interfaces que não usam.

### 🔴 Violação - ❌ ERRADO

```java
// Interface gigante
public interface CryptoManager {
    void buyCrypto();           // Só trader usa
    void sendNotification();    // Só notification usa
    void persistData();         // Só persistência usa
    void calculateTax();        // Só contador usa
    void generateReport();      // Só gerente usa
}

// Implementação precisa def tudo
@Service
public class CryptoService implements CryptoManager {
    @Override
    public void buyCrypto() { }
    
    @Override
    public void sendNotification() { }
    
    @Override
    public void persistData() { }
    
    @Override
    public void calculateTax() { }
    
    @Override
    public void generateReport() { }
}
```

### 🟢 Solução - ✅ CORRETO

```java
// Interfaces específicas
public interface CryptoTrading {
    void buyCrypto();
}

public interface Notifiable {
    void sendNotification();
}

public interface Persistable {
    void persistData();
}

public interface TaxCalculator {
    void calculateTax();
}

public interface Reportable {
    void generateReport();
}

// Cada classe implementa APENAS o que precisa
@Service
public class CryptoTradingService implements CryptoTrading {
    @Override
    public void buyCrypto() { }
}

@Service
public class NotificationService implements Notifiable {
    @Override
    public void sendNotification() { }
}

@Service
public class CryptoPersistenceService implements Persistable {
    @Override
    public void persistData() { }
}

// Cliente usa interface específica
public class TradingController {
    private final CryptoTrading tradingService; // Apenas o que precisa
    
    @PostMapping("/trade")
    public void trade() {
        tradingService.buyCrypto();
    }
}
```

### Backend Exemplo ✅

```java
// ✅ Interfaces específicas (já no projeto!)

public interface CryptoCurrencyService {
    Optional<CryptoCurrencyDTO> findById(String id);
    List<CryptoCurrencyDTO> findAll();
    void updateFromCoinGecko(String id);
}

public interface CoinGeckoApiService {
    Optional<CryptoCurrencyDTO> getCryptocurrencyData(String cryptoId);
    boolean isApiAvailable();
}

public interface DtoMapper {
    CryptoCurrencyDTO toDTO(Cryptocurrency cryptocurrency);
    Cryptocurrency toEntity(CryptoCurrencyDTO dto);
}

// Cada componente depende só do que precisa
@RestController
public class CryptoCurrencyController {
    private final CryptoCurrencyService service;  // Interface específica
    // Não conhece repository, mapper, etc
}
```

---

## 5. Dependency Inversion Principle (DIP)

### ✅ Conceito
Dependa de abstrações, não de concretudes.

### 🔴 Violação - ❌ ERRADO

```java
// ❌ Depende de implementação concreta
@Service
public class CryptoCurrencyService {
    private final CryptoCurrencyRepositoryPostgresImpl repository; // ❌ ERRADO!
    private final CoinGeckoApiServiceImpl apiService;             // ❌ ERRADO!
    
    public CryptoCurrencyService() {
        this.repository = new CryptoCurrencyRepositoryPostgresImpl();
        this.apiService = new CoinGeckoApiServiceImpl();
    }
}
```

**Problemas**:
- Acoplamento alto
- Difícil testar (impossível mockar)
- Se mudar implementação, quebra

### 🟢 Solução - ✅ CORRETO

```java
// ✅ Depende de abstração
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    private final CryptocurrencyRepository repository;  // Interface!
    private final CoinGeckoApiService apiService;      // Interface!
    
    // Injeção via construtor (Spring cuida)
    public CryptoCurrencyServiceImpl(
            CryptocurrencyRepository repository,
            CoinGeckoApiService apiService) {
        this.repository = repository;
        this.apiService = apiService;
    }
}
```

### Testes - Mockar Fácil ✅

```java
// ❌ Sem DIP - Impossível mockar
public class CryptoServiceTest {
    private CryptoCurrencyService service;
    // Não consegue testar isolado!
}

// ✅ Com DIP - Mockar fácil
public class CryptoServiceTest {
    
    @Mock
    private CryptocurrencyRepository repository;
    
    @Mock
    private CoinGeckoApiService apiService;
    
    @InjectMocks
    private CryptoCurrencyServiceImpl service;
    
    @Test
    public void testFindById() {
        Cryptocurrency mockCrypto = new Cryptocurrency();
        mockCrypto.setId("bitcoin");
        
        when(repository.findById("bitcoin")).thenReturn(Optional.of(mockCrypto));
        
        Optional<CryptoCurrencyDTO> result = service.findById("bitcoin");
        
        assertTrue(result.isPresent());
        assertEquals("bitcoin", result.get().getId());
    }
}
```

### Frontend - React Exemplo ✅

```javascript
// ❌ ERRADO - Depende de implementação
function SearchBar() {
    const apiClient = new HttpClient(); // ❌ Acoplado!
    
    const handleSearch = async (term) => {
        const results = await apiClient.get(`/api/search?term=${term}`);
        setResults(results);
    };
}

// ✅ CORRETO - Depende de abstração (service)
import { searchCryptos } from '../../services/apiService';

function SearchBar() {
    const handleSearch = async (term) => {
        const results = await searchCryptos(term); // Service (abstração)
        setResults(results);
    };
}

// Mock em testes
jest.mock('../../services/apiService', () => ({
    searchCryptos: jest.fn(() => Promise.resolve([
        { id: 'bitcoin', name: 'Bitcoin' }
    ]))
}));
```

---

## 🎯 Resumo SOLID

| Princípio | Benefício | Índice |
|-----------|----------|--------|
| **SRP** | Uma razão para mudar | ⭐⭐⭐⭐⭐ |
| **OCP** | Extensível sem modificar | ⭐⭐⭐⭐⭐ |
| **LSP** | Substituição confiável | ⭐⭐⭐⭐ |
| **ISP** | Interfaces focadas | ⭐⭐⭐⭐ |
| **DIP** | Baixo acoplamento | ⭐⭐⭐⭐⭐ |

---

## 📋 Checklist SOLID no Código

- [ ] Cada classe tem UMA responsabilidade?
- [ ] Pode adicionar nova funcionalidade SEM modificar código?
- [ ] Subclasses respeitam contrato?
- [ ] Interfaces são específicas (não gigantes)?
- [ ] Depende de interfaces, não implementações concretas?

---

## 📚 Referências

- [Baeldung - SOLID](https://www.baeldung.com/solid-principles)
- [Uncle Bob - SOLID](https://blog.cleancoder.com/uncle-bob/2020/10/18/Solid-Relevance.html)
- [Design Patterns - SOLID](https://refactoring.guru/design-patterns/solid)
