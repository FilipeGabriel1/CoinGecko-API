# Backend - CoinGecko API

## Descrição
Backend em Java com Spring Boot que integra dados da API CoinGecko com banco de dados e expõe APIs REST para o Frontend.

## Padrões de Projeto Implementados

### 1. **Repository Pattern** 
- **Localização**: `com.coingecko.repository`
- **Propósito**: Abstração de acesso a dados
- **Classes**:
  - `CryptocurrencyRepository`
  - `CryptoCurrencyPriceRepository`
- **Benefício**: Facilita testes e troca de banco de dados

### 2. **Service Pattern**
- **Localização**: `com.coingecko.service`
- **Propósito**: Encapsular lógica de negócio
- **Classes**:
  - `CryptoCurrencyService` (interface)
  - `CryptoCurrencyServiceImpl` (implementação)
- **Benefício**: Lógica de negócio centralizada e reutilizável

### 3. **Adapter Pattern**
- **Localização**: `com.coingecko.service.impl`
- **Propósito**: Adaptar API CoinGecko para nossa aplicação
- **Classes**:
  - `CoinGeckoApiService` (interface)
  - `CoinGeckoApiServiceImpl` (implementação)
- **Benefício**: Isolamento da integração externa

### 4. **Singleton Pattern**
- **Localização**: `com.coingecko.config`
- **Propósito**: Garantir instância única de beans
- **Classes**:
  - `ApplicationConfig` (Spring Beans)
- **Benefício**: Controle de instâncias críticas

### 5. **Factory Pattern**
- **Localização**: `com.coingecko.config`
- **Propósito**: Criar instâncias de beans complexos
- **Métodos**:
  - `restTemplate()` - Factory para RestTemplate
  - `objectMapper()` - Factory para ObjectMapper
- **Benefício**: Centralização da criação de objetos

### 6. **Mapper/Builder Pattern**
- **Localização**: `com.coingecko.service`
- **Propósito**: Transformação entre entidades e DTOs
- **Classes**:
  - `DtoMapper` (interface)
  - `CryptoCurrencyDtoMapperImpl` (implementação)
- **Benefício**: Separação entre modelo de dados e apresentação

---

## Princípios SOLID

### S - Single Responsibility Principle
Cada classe tem uma única responsabilidade:
- `Cryptocurrency` - representa entidade
- `CryptoCurrencyService` - lógica de negócio
- `CryptoCurrencyController` - requisições HTTP

### O - Open/Closed Principle
Aberto para extensão, fechado para modificação:
- Novas estratégias de API podem ser implementadas sem modificar código existente
- Interface `CoinGeckoApiService` permite múltiplas implementações

### L - Liskov Substitution Principle
Subtipos podem substituir supertipos:
- `CryptoCurrencyServiceImpl` implementa `CryptoCurrencyService`
- Qualquer implementação de `DtoMapper` pode ser usada

### I - Interface Segregation Principle
Clientes não devem depender de interfaces que não usam:
- `CryptoCurrencyService` - interface específica
- `CoinGeckoApiService` - interface específica
- `DtoMapper` - interface específica

### D - Dependency Inversion Principle
Depender de abstrações, não de concretudes:
```java
// ✅ BOM: Depende de interface
private final CryptoCurrencyService service;

// ❌ RUIM: Depende de implementação
private final CryptoCurrencyServiceImpl service;
```

---

## Arquitetura

```
com.coingecko/
├── CoinGeckoApplication.java (главное приложение)
├── api/
│   └── CryptoCurrencyController.java (REST endpoints)
├── model/
│   ├── Cryptocurrency.java (entidade)
│   └── CryptoCurrencyPrice.java (entidade)
├── dto/
│   ├── CryptoCurrencyDTO.java (transfer object)
│   └── CryptoPriceDTO.java (transfer object)
├── repository/
│   ├── CryptocurrencyRepository.java (Repository Pattern)
│   └── CryptoCurrencyPriceRepository.java (Repository Pattern)
├── service/
│   ├── CryptoCurrencyService.java (interface)
│   ├── CoinGeckoApiService.java (interface)
│   ├── DtoMapper.java (interface)
│   └── impl/
│       ├── CryptoCurrencyServiceImpl.java (implementação)
│       ├── CoinGeckoApiServiceImpl.java (implementação)
│       └── CryptoCurrencyDtoMapperImpl.java (implementação)
└── config/
    └── ApplicationConfig.java (configuração de beans)
```

---

## Endpoints da API

### Buscar por ID
```bash
GET /api/v1/cryptocurrencies/{id}
```

### Buscar por Símbolo
```bash
GET /api/v1/cryptocurrencies/symbol/{symbol}
```

### Listar Todas
```bash
GET /api/v1/cryptocurrencies
```

### Buscar por Símbolo (parcial)
```bash
GET /api/v1/cryptocurrencies/search?symbol=BTC
```

### Top 10 Criptomoedas
```bash
GET /api/v1/cryptocurrencies/top
```

### Criar Criptomoeda
```bash
POST /api/v1/cryptocurrencies
Content-Type: application/json

{
  "id": "bitcoin",
  "symbol": "BTC",
  "name": "Bitcoin",
  "description": "Primeira criptomoeda",
  "imageUrl": "...",
  "marketCapRank": 1,
  "price": { ... }
}
```

### Deletar Criptomoeda
```bash
DELETE /api/v1/cryptocurrencies/{id}
```

### Atualizar de CoinGecko
```bash
PUT /api/v1/cryptocurrencies/{id}/update
```

---

## Banco de Dados

### Desenvolvimento
- **H2 Database** (em memória)
- Console disponível em: `http://localhost:8080/h2-console`

### Produção
- **PostgreSQL**
- Alterar em `application.yml`

### Schema

```sql
cryptocurrencies
├── id (string, PK)
├── symbol (string, unique)
├── name (string)
├── description (text)
├── imageUrl (text)
├── marketCapRank (long)
└── lastUpdated (timestamp)

cryptocurrency_prices
├── id (long, PK)
├── cryptocurrency_id (FK)
├── priceUsd (decimal)
├── priceEur (decimal)
├── priceBrl (decimal)
├── volume24hUsd (decimal)
├── priceChangePercentage24h (decimal)
├── marketCap (decimal)
└── timestamp (timestamp)
```

---

## Como Executar

### Build
```bash
mvn clean package
```

### Executar
```bash
mvn spring-boot:run
```

### Com Maven Wrapper
```bash
./mvnw spring-boot:run
```

---

## Tecnologias

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **H2 Database**
- **PostgreSQL Driver**
- **Lombok**
- **Jackson**
- **Maven**

---

## Integração com CoinGecko API

A aplicação consome dados da **CoinGecko API** (`https://api.coingecko.com/api/v3`) para:
- Obter preços em múltiplas moedas (USD, EUR, BRL)
- Atualizar volumes de 24h
- Obter mudanças percentuais de preço
- Market cap e ranking

---

## Próximos Passos

1. Implementar autenticação JWT
2. Adicionar cache com Redis
3. Implementar WebSocket para atualizações em tempo real
4. Adicionar testes unitários e de integração
5. Documentação com Swagger/OpenAPI
