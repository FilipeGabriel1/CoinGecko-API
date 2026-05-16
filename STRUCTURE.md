# 📂 Estrutura Completa do Projeto

## Árvore de Diretórios

```
CoinGecko API/
│
├── 📖 README.md                    ← COMECE AQUI (visão geral)
├── 📖 QUICKSTART.md                ← Guia 5 minutos
├── 📖 PATTERNS.md                  ← Padrões explicados
├── 📖 SOLID.md                     ← SOLID com exemplos
├── 📖 SUMMARY.md                   ← Resumo executivo
│
├── 🐘 Backend/                     ← Java/Spring Boot
│   │
│   ├── src/main/java/com/coingecko/
│   │   ├── 🎯 CoinGeckoApplication.java
│   │   │   └─ @SpringBootApplication
│   │   │
│   │   ├── 📡 api/
│   │   │   └─ CryptoCurrencyController.java
│   │   │      ├─ GET /{id}
│   │   │      ├─ GET /symbol/{symbol}
│   │   │      ├─ GET /
│   │   │      ├─ GET /search
│   │   │      ├─ GET /top
│   │   │      ├─ POST /
│   │   │      ├─ DELETE /{id}
│   │   │      └─ PUT /{id}/update
│   │   │
│   │   ├── 📊 model/
│   │   │   ├─ Cryptocurrency.java       (Entidade JPA)
│   │   │   │  ├─ id (PK)
│   │   │   │  ├─ symbol
│   │   │   │  ├─ name
│   │   │   │  ├─ currentPrice (OneToOne)
│   │   │   │  └─ lastUpdated
│   │   │   │
│   │   │   └─ CryptoCurrencyPrice.java  (Entidade JPA)
│   │   │      ├─ id (PK)
│   │   │      ├─ cryptocurrency (FK)
│   │   │      ├─ priceUsd
│   │   │      ├─ priceEur
│   │   │      ├─ priceBrl
│   │   │      ├─ volume24hUsd
│   │   │      ├─ priceChangePercentage24h
│   │   │      ├─ marketCap
│   │   │      └─ timestamp
│   │   │
│   │   ├── 📦 dto/
│   │   │   ├─ CryptoCurrencyDTO.java
│   │   │   │  ├─ id
│   │   │   │  ├─ symbol
│   │   │   │  ├─ name
│   │   │   │  ├─ description
│   │   │   │  ├─ price (CryptoPriceDTO)
│   │   │   │  └─ lastUpdated
│   │   │   │
│   │   │   └─ CryptoPriceDTO.java
│   │   │      ├─ priceUsd
│   │   │      ├─ priceEur
│   │   │      ├─ priceBrl
│   │   │      ├─ volume24hUsd
│   │   │      ├─ priceChangePercentage24h
│   │   │      ├─ marketCap
│   │   │      └─ timestamp
│   │   │
│   │   ├── 🗄️ repository/
│   │   │   ├─ CryptocurrencyRepository.java
│   │   │   │  ├─ JpaRepository<Cryptocurrency, String>
│   │   │   │  ├─ findBySymbol()
│   │   │   │  ├─ findBySymbolContainingIgnoreCase()
│   │   │   │  └─ findByOrderByMarketCapRankAsc()
│   │   │   │
│   │   │   └─ CryptoCurrencyPriceRepository.java
│   │   │      ├─ JpaRepository<CryptoCurrencyPrice, Long>
│   │   │      └─ findByCryptocurrency_Id()
│   │   │
│   │   ├── 🔧 service/
│   │   │   ├── 📝 Interfaces:
│   │   │   │   ├─ CryptoCurrencyService.java
│   │   │   │   │  ├─ findById()
│   │   │   │   │  ├─ findBySymbol()
│   │   │   │   │  ├─ findAll()
│   │   │   │   │  ├─ searchBySymbol()
│   │   │   │   │  ├─ getTopCryptos()
│   │   │   │   │  ├─ save()
│   │   │   │   │  ├─ delete()
│   │   │   │   │  └─ updateFromCoinGecko()
│   │   │   │   │
│   │   │   │   ├─ CoinGeckoApiService.java
│   │   │   │   │  ├─ getCryptocurrencyData()
│   │   │   │   │  └─ isApiAvailable()
│   │   │   │   │
│   │   │   │   └─ DtoMapper.java
│   │   │   │      ├─ toDTO()
│   │   │   │      └─ toEntity()
│   │   │   │
│   │   │   └── 💻 impl/ (Implementações)
│   │   │       ├─ CryptoCurrencyServiceImpl.java
│   │   │       │  └─ @Service (Service Pattern)
│   │   │       │     ├─ Coordena lógica
│   │   │       │     ├─ Usa repositories
│   │   │       │     ├─ Usa API service
│   │   │       │     └─ Usa mapper
│   │   │       │
│   │   │       ├─ CoinGeckoApiServiceImpl.java
│   │   │       │  └─ @Service (Adapter Pattern)
│   │   │       │     ├─ Consome CoinGecko API
│   │   │       │     ├─ Parseia resposta
│   │   │       │     └─ Transforma em DTO
│   │   │       │
│   │   │       └─ CryptoCurrencyDtoMapperImpl.java
│   │   │          └─ @Component (Mapper Pattern)
│   │   │             ├─ Converte Entity → DTO
│   │   │             └─ Converte DTO → Entity
│   │   │
│   │   └── ⚙️ config/
│   │       └─ ApplicationConfig.java
│   │          └─ @Configuration (Factory + Singleton)
│   │             ├─ @Bean restTemplate()
│   │             └─ @Bean objectMapper()
│   │
│   ├── src/main/resources/
│   │   └─ application.yml
│   │      ├─ spring.jpa
│   │      ├─ spring.datasource
│   │      ├─ logging
│   │      ├─ coingecko.api
│   │      └─ server
│   │
│   ├── 📄 pom.xml (Maven - 10+ dependencies)
│   ├── 🐳 Dockerfile (Multi-stage build)
│   ├── .gitignore
│   └── 📖 README.md (Específico backend)
│
│
├── ⚛️ Frontend/                    ← React/Vite
│   │
│   ├── src/
│   │   ├── 🎯 App.jsx
│   │   │   └─ Raiz da aplicação (Provider)
│   │   │
│   │   ├── 🚀 main.jsx
│   │   │   └─ Ponto de entrada (ReactDOM)
│   │   │
│   │   ├── 📄 components/
│   │   │   ├── SearchBar/
│   │   │   │   ├─ SearchBar.jsx
│   │   │   │   │  ├─ Input de busca
│   │   │   │   │  ├─ Debounced search
│   │   │   │   │  └─ setSearchResults()
│   │   │   │   └─ SearchBar.css
│   │   │   │
│   │   │   ├── CryptoCard/
│   │   │   │   ├─ CryptoCard.jsx
│   │   │   │   │  ├─ Render cripto individual
│   │   │   │   │  ├─ Preços em 3 moedas
│   │   │   │   │  ├─ Botão update
│   │   │   │   │  ├─ Botão delete
│   │   │   │   │  └─ Mudança percentual
│   │   │   │   └─ CryptoCard.css
│   │   │   │
│   │   │   └── CryptoList/
│   │   │       ├─ CryptoList.jsx
│   │   │       │  ├─ Render lista
│   │   │       │  ├─ View mode toggle
│   │   │       │  ├─ Loading state
│   │   │       │  ├─ Error handling
│   │   │       │  └─ Map CryptoCard
│   │   │       └─ CryptoList.css
│   │   │
│   │   ├── 📄 pages/
│   │   │   └─ Home.jsx
│   │   │      ├─ Header com backgroundgrad
│   │   │      ├─ SearchBar component
│   │   │      ├─ CryptoList component
│   │   │      ├─ Footer
│   │   │      └─ Main layout
│   │   │
│   │   ├── 🧠 context/
│   │   │   └─ CryptoContext.jsx
│   │   │      ├─ createContext()
│   │   │      ├─ CryptoProvider (comp)
│   │   │      ├─ cryptoReducer() (10 actions)
│   │   │      │  ├─ SET_CRYPTOS
│   │   │      │  ├─ SET_SEARCH_RESULTS
│   │   │      │  ├─ SET_SELECTED_CRYPTO
│   │   │      │  ├─ SET_LOADING
│   │   │      │  ├─ SET_ERROR
│   │   │      │  ├─ SET_FILTER
│   │   │      │  ├─ SET_SORT_BY
│   │   │      │  ├─ ADD_CRYPTO
│   │   │      │  ├─ REMOVE_CRYPTO
│   │   │      │  └─ UPDATE_CRYPTO
│   │   │      └─ useCrypto() (custom hook)
│   │   │
│   │   ├── 🔗 services/
│   │   │   └─ apiService.js
│   │   │      ├─ axios.create (client)
│   │   │      ├─ getCryptoById()
│   │   │      ├─ getCryptoBySymbol()
│   │   │      ├─ getAllCryptos()
│   │   │      ├─ searchCryptos()
│   │   │      ├─ getTopCryptos()
│   │   │      ├─ createCrypto()
│   │   │      ├─ deleteCrypto()
│   │   │      └─ updateFromCoinGecko()
│   │   │
│   │   ├── 🎨 styles/
│   │   │   ├─ global.css (reset, utilities)
│   │   │   └─ Home.css (page styles)
│   │   │
│   │   └─ (CSS modules em cada component)
│   │
│   ├── public/
│   │   └─ index.html
│   │      └─ <div id="root"></div>
│   │
│   ├── 📄 package.json
│   │   ├─ dependencies
│   │   │  ├─ react: ^18.2.0
│   │   │  ├─ react-dom: ^18.2.0
│   │   │  ├─ axios: ^1.4.0
│   │   │  └─ react-router-dom: ^6.14.0
│   │   └─ devDependencies
│   │      ├─ @vitejs/plugin-react
│   │      ├─ vite
│   │      └─ eslint
│   │
│   ├── ⚙️ vite.config.js
│   │   ├─ react plugin
│   │   ├─ proxy para backend
│   │   └─ port 3000
│   │
│   ├── 🐳 Dockerfile (Node multi-stage)
│   ├── .gitignore
│   └── 📖 README.md (Específico frontend)
│
│
├── 🐳 docker-compose.yml
│   ├─ postgres service
│   ├─ backend service
│   └─ frontend service
│
├── 📚 PADRÕES IMPLEMENTADOS:
│   ├─ Repository Pattern (Data)
│   ├─ Service Pattern (Logic)
│   ├─ Adapter Pattern (API)
│   ├─ Factory Pattern (Config)
│   ├─ Singleton Pattern (Beans)
│   ├─ Mapper Pattern (DTO)
│   ├─ Context Pattern (React)
│   ├─ Reducer Pattern (State)
│   └─ Composition Pattern (UI)
│
├── 📚 PRINCÍPIOS SOLID:
│   ├─ Single Responsibility
│   ├─ Open/Closed
│   ├─ Liskov Substitution
│   ├─ Interface Segregation
│   └─ Dependency Inversion
│
└── 📊 FLUXO DE DADOS:
    ├─ Frontend → Service → API → Backend
    ├─ Backend Controller → Service → Repository → DB
    ├─ DB → Repository → Service → Controller → Frontend
    └─ External API (CoinGecko) ← Adapter Service
```

---

## 📊 Estatísticas de Código

### Backend Java
```
Component          Files  Classes  Methods  LOC
────────────────────────────────────────────────
Controllers         1      1        8       150
Services           3      3        30      400
Repositories       2      2        5       30
Models             2      2        0       50
DTOs               2      2        0       40
Config             1      1        2       50
────────────────────────────────────────────────
TOTAL             11     11        45      720
```

### Frontend React
```
Component          Files  Functions  LOC
─────────────────────────────────────────
Pages              1      1         80
Components         3      3        300
Context            1      1        200
Services           1      9        150
Styles             5      0        500
Config             2      0        50
─────────────────────────────────────────
TOTAL             13     14       1280
```

### Documentação
```
Document           Type    LOC
──────────────────────────────
README.md          MD     300
PATTERNS.md        MD     600
SOLID.md           MD     700
QUICKSTART.md      MD     400
SUMMARY.md         MD     500
Backend/README.md  MD     300
Frontend/README.md MD     400
──────────────────────────────
TOTAL              MD    3200
```

---

## 🎯 Mapa de Navegação

### Para Aprender Padrões
1. Leia `PATTERNS.md`
2. Localize em `/Backend/src/main/java/...`
3. Veja código com comentários
4. Entenda a relação com SOLID

### Para Aprender SOLID
1. Leia `SOLID.md`
2. Veja exemplos ❌ ERRADO vs ✅ CORRETO
3. Relacione com padrões
4. Implemente mudanças

### Para Rodar Projeto
1. Leia `QUICKSTART.md`
2. Siga passos rápidos
3. Se erro, consulte Troubleshooting
4. Debugue com DevTools

### Para Explorar Código
1. Comece em `CoinGeckoApplication.java`
2. Siga o fluxo até `CryptoCurrencyController`
3. Veja como `Service` coordena
4. Entenda como `Repository` acessa DB

---

## 📌 Checklist de Implementação

### ✅ Backend
- [x] Entidades JPA (2)
- [x] DTOs (2)
- [x] Repositories (2)
- [x] Service Interfaces (3)
- [x] Service Implementations (3)
- [x] REST Controller (1)
- [x] Configuration (1)
- [x] Banco de dados (H2)
- [x] Logging
- [x] Error handling

### ✅ Frontend
- [x] Componentes (3)
- [x] Pages (1)
- [x] Context + Reducer
- [x] API Service
- [x] Estilos (global + local)
- [x] Loading states
- [x] Error handling
- [x] Search funcional
- [x] Responsivo

### ✅ Padrões
- [x] Repository Pattern
- [x] Service Pattern
- [x] Adapter Pattern
- [x] Factory Pattern
- [x] Singleton Pattern
- [x] Mapper Pattern
- [x] Context Pattern
- [x] Reducer Pattern
- [x] Composition Pattern

### ✅ SOLID
- [x] Single Responsibility
- [x] Open/Closed
- [x] Liskov Substitution
- [x] Interface Segregation
- [x] Dependency Inversion

### ✅ Documentação
- [x] README principal
- [x] PATTERNS.md
- [x] SOLID.md
- [x] QUICKSTART.md
- [x] SUMMARY.md
- [x] Backend README
- [x] Frontend README
- [x] Comentários em código

### ✅ DevOps
- [x] Dockerfile (Backend)
- [x] Dockerfile (Frontend)
- [x] docker-compose.yml
- [x] .gitignore (ambos)
- [x] application.yml

---

## 🌟 Destaques

⭐ **Backend bem estruturado** com múltiplas camadas  
⭐ **Frontend com state management** profissional  
⭐ **Documentação completa** com exemplos  
⭐ **Padrões visíveis** no código  
⭐ **SOLID aplicado** praticamente  
⭐ **Pronto para extensão** e manutenção  
⭐ **Educacional e profissional**  

---

**_Projeto criado com propósito educacional_**

**_Data: 16 de Maio de 2026_**

**_Status: ✅ COMPLETO_**
