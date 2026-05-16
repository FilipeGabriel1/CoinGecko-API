# 🏗️ Padrões de Projeto Implementados

## Backend (Java/Spring Boot)

### 1. Repository Pattern ⭐⭐⭐⭐⭐
**Localização**: `com.coingecko.repository`

```java
// Interface para abstração
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, String> {
    Optional<Cryptocurrency> findBySymbol(String symbol);
}

// Uso no serviço (Dependency Injection)
public class CryptoCurrencyServiceImpl {
    private final CryptocurrencyRepository repository;
    
    public Optional<CryptoCurrencyDTO> findBySymbol(String symbol) {
        return repository.findBySymbol(symbol).map(dtoMapper::toDTO);
    }
}
```

**Benefícios**:
- Abstração de persistência
- Facilita testes (mockar repository)
- Possibilita trocar banco de dados facilmente

---

### 2. Service Pattern ⭐⭐⭐⭐⭐
**Localização**: `com.coingecko.service`

```java
// Interface define contrato
public interface CryptoCurrencyService {
    Optional<CryptoCurrencyDTO> findById(String id);
    List<CryptoCurrencyDTO> findAll();
    void updateFromCoinGecko(String id);
}

// Implementação encapsula lógica
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    // Lógica de negócio centralizada
    @Override
    public void updateFromCoinGecko(String id) {
        Optional<CryptoCurrencyDTO> apiData = coinGeckoApiService.getCryptocurrencyData(id);
        // ... processamento
    }
}
```

**Benefícios**:
- Lógica de negócio centralizada
- Reutilizável em múltiplos controllers
- Fácil de testar

---

### 3. Adapter Pattern ⭐⭐⭐⭐
**Localização**: `com.coingecko.service.impl.CoinGeckoApiServiceImpl`

```java
// Adapter converte API externa para nossa aplicação
@Service
public class CoinGeckoApiServiceImpl implements CoinGeckoApiService {
    @Override
    public Optional<CryptoCurrencyDTO> getCryptocurrencyData(String cryptoId) {
        // Adapter: transforma resposta da API CoinGecko
        String response = restTemplate.getForObject(url, String.class);
        JsonNode data = objectMapper.readTree(response);
        return parseApiResponse(cryptoId, data);
    }
}
```

**Benefícios**:
- Desacopla cliente da API externa
- Se CoinGecko mudar, altera só aqui
- Pode ter múltiplos adapters para diferentes APIs

---

### 4. Factory Pattern ⭐⭐⭐⭐
**Localização**: `com.coingecko.config.ApplicationConfig`

```java
@Configuration
public class ApplicationConfig {
    
    // Factory criando RestTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }
    
    // Factory criando ObjectMapper
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
}
```

**Benefícios**:
- Centraliza criação de objetos complexos
- Configuração reutilizável
- Spring garante Singleton automaticamente

---

### 5. Singleton Pattern ⭐⭐⭐⭐⭐
**Localização**: `com.coingecko.config` (via Spring Beans)

```java
// Spring automaticamente faz Singleton
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    // Instância única em toda aplicação
}

// Injeção em múltiplos pontos
@RestController
public class CryptoCurrencyController {
    private final CryptoCurrencyService service; // Uma única instância
}
```

**Benefícios**:
- Economiza memória
- Evita conflitos de estado
- Spring gerencia automaticamente

---

### 6. Mapper/Builder Pattern ⭐⭐⭐⭐
**Localização**: `com.coingecko.service.impl.CryptoCurrencyDtoMapperImpl`

```java
@Component
public class CryptoCurrencyDtoMapperImpl implements DtoMapper {
    
    @Override
    public CryptoCurrencyDTO toDTO(Cryptocurrency crypto) {
        CryptoCurrencyDTO dto = new CryptoCurrencyDTO();
        dto.setId(crypto.getId());
        dto.setName(crypto.getName());
        // ... mapping
        return dto;
    }
}
```

**Benefícios**:
- Separa modelo de apresentação
- Reutilizável
- Fácil de manter

---

## Frontend (React)

### 1. Context Pattern (Observer) ⭐⭐⭐⭐⭐
**Localização**: `src/context/CryptoContext.jsx`

```javascript
export function CryptoProvider({ children }) {
    const [state, dispatch] = useReducer(cryptoReducer, initialState);
    
    return (
        <CryptoContext.Provider value={{ state, setCryptos, ... }}>
            {children}
        </CryptoContext.Provider>
    );
}

// Uso em componentes
function SearchBar() {
    const { setSearchResults } = useCrypto();
    // Acessa contexto global
}
```

**Benefícios**:
- Evita prop drilling
- Estado centralizado
- Similar a Redux mas mais simples

---

### 2. Reducer Pattern (State Machine) ⭐⭐⭐⭐⭐
**Localização**: `src/context/CryptoContext.jsx`

```javascript
function cryptoReducer(state, action) {
    switch (action.type) {
        case 'SET_CRYPTOS':
            return { ...state, cryptos: action.payload };
        case 'SET_LOADING':
            return { ...state, loading: action.payload };
        case 'SET_ERROR':
            return { ...state, error: action.payload };
        // ...
    }
}
```

**Benefícios**:
- Transições de estado previsíveis
- Fácil de debugar
- Testável

---

### 3. Service Pattern ⭐⭐⭐⭐⭐
**Localização**: `src/services/apiService.js`

```javascript
const API_BASE_URL = 'http://localhost:8080/api/v1/cryptocurrencies';

export const getCryptoById = async (id) => {
    try {
        const response = await apiClient.get(`/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Erro ao buscar cripto ${id}:`, error);
        throw error;
    }
};
```

**Benefícios**:
- Abstrai comunicação HTTP
- Reutilizável em múltiplos componentes
- Fácil mudar backend

---

### 4. Composition Pattern ⭐⭐⭐⭐⭐
**Localização**: `src/components/`

```javascript
function Home() {
    return (
        <>
            <header>Header</header>
            <SearchBar />          {/* Composição */}
            <CryptoList />         {/* Composição */}
            <footer>Footer</footer>
        </>
    );
}

function CryptoList() {
    return cryptos.map(crypto => 
        <CryptoCard crypto={crypto} /> {/* Composição */}
    );
}
```

**Benefícios**:
- Componentes pequenos e reutilizáveis
- Fácil de testar isoladamente
- Flexibilidade

---

### 5. Container vs Presentational ⭐⭐⭐⭐
**Localização**: Separação entre lógica e UI

```javascript
// Container (Smart Component)
function CryptoList() {
    const { state, setSearchResults } = useCrypto();
    useEffect(() => { /* carregar dados */ }, []);
    return <CryptoListUI data={cryptos} />;
}

// Presentational (Dumb Component)
function CryptoCard({ crypto, onSelect }) {
    return (
        <div className="card">
            <h3>{crypto.name}</h3>
            <button onClick={() => onSelect(crypto)}>Ver</button>
        </div>
    );
}
```

**Benefícios**:
- Separação de responsabilidades
- Componentes mais testáveis
- Melhor reutilização

---

## Resumo Comparativo

| Padrão | Backend | Frontend | Propósito |
|--------|---------|----------|-----------|
| **Repository** | ✅ | - | Abstração de dados |
| **Service** | ✅ | ✅ | Lógica de negócio |
| **Adapter** | ✅ | - | Integração externa |
| **Factory** | ✅ | - | Criação de objetos |
| **Singleton** | ✅ | ✅ | Instância única |
| **Context** | - | ✅ | Estado global |
| **Reducer** | - | ✅ | State machine |
| **Composition** | - | ✅ | Componentes |
| **Service (HTTP)** | - | ✅ | HTTP abstraction |

---

## 🎯 Quando Usar Cada Padrão

| Situação | Padrão |
|----------|--------|
| Precisa mudar banco dados | Repository |
| Lógica de negócio complexa | Service |
| Integrar API externa | Adapter |
| Criar objetos complexos | Factory |
| Apenas uma instância necessária | Singleton |
| Múltiplos componentes acessam dados | Context |
| Transições de estado | Reducer |
| Componentes reutilizáveis | Composition |
| Comunicação HTTP | Service |

---

## 📚 Recursos Adicionais

- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [React Patterns](https://reactpatterns.com)
- [Spring Framework Best Practices](https://spring.io)
