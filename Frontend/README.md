# Frontend - CoinGecko API

## Descrição
Frontend em React que consome a API do backend para exibir informações de criptomoedas com grande experiência de usuário.

## Padrões de Projeto Implementados

### 1. **Context Pattern (Observer Pattern)**
- **Localização**: `src/context/CryptoContext.jsx`
- **Propósito**: Gerenciamento de estado global
- **Benefício**: Evita prop drilling, estado centralizado

### 2. **Reducer Pattern (State Machine)**
- **Localização**: `src/context/CryptoContext.jsx`
- **Propósito**: Gerenciamento previsível de estado
- **Benefício**: Transições de estado explícitas e testáveis

### 3. **Service/API Service Pattern**
- **Localização**: `src/services/apiService.js`
- **Propósito**: Abstração de requisições HTTP
- **Benefício**: Isolamento da comunicação com backend

### 4. **Component Composition**
- **Localização**: `src/components/`
- **Propósito**: Reutilização de componentes
- **Componentes**:
  - `SearchBar` - busca e filtro
  - `CryptoCard` - exibição individual
  - `CryptoList` - listagem
  - `Home` - página principal
- **Benefício**: Componentes pequenos, testáveis e reutilizáveis

### 5. **Container vs Presentational**
- **Containers**: `Home` (lógica), `CryptoList` (dados)
- **Presentational**: `CryptoCard`, `SearchBar` (UI pura)
- **Benefício**: Separação entre lógica e apresentação

### 6. **Singleton Pattern (Custom Hook)**
- **Localização**: `src/context/CryptoContext.jsx`
- **Hook**: `useCrypto()`
- **Propósito**: Acesso singleton ao contexto
- **Benefício**: Interface simplificada para contexto

---

## Princípios SOLID

### S - Single Responsibility Principle
- `CryptoCard` - apenas renderiza um card
- `SearchBar` - apenas busca
- `CryptoList` - apenas lista
- `CryptoContext` - apenas estado global

### O - Open/Closed Principle
- Novos componentes podem ser adicionados sem modificar existentes
- Serviço API pode ser facilmente substituído

### L - Liskov Substitution Principle
- Qualquer componente que implemente interface pode ser substituído

### I - Interface Segregation Principle
- Componentes recebem apenas props necessárias
- Context expõe apenas métodos necessários

### D - Dependency Inversion Principle
```javascript
// ✅ BOM: Depende de serviço abstrato
import { searchCryptos } from '../../services/apiService';

// ❌ RUIM: Acesso direto a implementação
fetch('http://localhost:8080/...')
```

---

## Estrutura de Pastas

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── SearchBar/
│   │   │   ├── SearchBar.jsx
│   │   │   └── SearchBar.css
│   │   ├── CryptoCard/
│   │   │   ├── CryptoCard.jsx
│   │   │   └── CryptoCard.css
│   │   └── CryptoList/
│   │       ├── CryptoList.jsx
│   │       └── CryptoList.css
│   ├── context/
│   │   └── CryptoContext.jsx (State Management)
│   ├── pages/
│   │   └── Home.jsx
│   ├── services/
│   │   └── apiService.js (API Integration)
│   ├── styles/
│   │   ├── global.css
│   │   └── Home.css
│   ├── App.jsx
│   └── main.jsx
├── package.json
├── vite.config.js
└── index.html
```

---

## Componentes

### SearchBar
```jsx
<SearchBar />
// Busca criptomoedas e atualiza resultados
// Usa: CryptoContext.setSearchResults()
```

### CryptoCard
```jsx
<CryptoCard crypto={cryptoData} onSelect={handler} />
// Exibe informações de uma criptomoeda
// Props: crypto (Object), onSelect (Function)
// Ações: Atualizar, Deletar
```

### CryptoList
```jsx
<CryptoList onSelectCrypto={handler} />
// Lista todas as criptomoedas
// Modo: Todas / Top 10
// Integra SearchBar + CryptoCard
```

### Home (Página Principal)
```jsx
<Home />
// Combina todos os componentes
// Estado selecionado de criptomoeda
```

---

## State Management (Context)

### Estado Global
```javascript
{
  cryptos: [],              // todas as criptomoedas
  searchResults: [],        // resultados da busca
  selectedCrypto: null,     // cripto selecionada
  loading: false,           // carregando?
  error: null,              // mensagem de erro
  filter: '',               // filtro atual
  sortBy: 'marketCapRank'   // ordenação
}
```

### Actions
```javascript
setCryptos(cryptos)           // define lista
setSearchResults(results)     // define resultados
setSelectedCrypto(crypto)     // define selecionada
setLoading(boolean)           // define carregando
setError(error)               // define erro
setFilter(filter)             // define filtro
setSortBy(field)              // define ordenação
addCrypto(crypto)             // adiciona uma
removeCrypto(id)              // remove uma
updateCrypto(crypto)          // atualiza uma
```

---

## API Integration

### Endpoints consumidos

```javascript
// GET /api/v1/cryptocurrencies/:id
getCryptoById(id)

// GET /api/v1/cryptocurrencies/symbol/:symbol
getCryptoBySymbol(symbol)

// GET /api/v1/cryptocurrencies
getAllCryptos()

// GET /api/v1/cryptocurrencies/search?symbol=BTC
searchCryptos(symbol)

// GET /api/v1/cryptocurrencies/top
getTopCryptos()

// POST /api/v1/cryptocurrencies
createCrypto(crypto)

// DELETE /api/v1/cryptocurrencies/:id
deleteCrypto(id)

// PUT /api/v1/cryptocurrencies/:id/update
updateFromCoinGecko(id)
```

---

## Como Executar

### Instalação
```bash
npm install
```

### Desenvolvimento
```bash
npm run dev
```
Abre em `http://localhost:3000`

### Build
```bash
npm run build
```

### Preview
```bash
npm run preview
```

---

## Tecnologias

- **React 18.2.0** - UI
- **Vite** - Build tool
- **Axios** - HTTP client
- **React Router** - Roteamento (opcional)
- **CSS3** - Estilos

---

## Funcionalidades

✅ Listar todas as criptomoedas  
✅ Buscar criptomoedas por símbolo  
✅ Ver preços em USD, EUR, BRL  
✅ Visualizar mudanças percentuais  
✅ Top 10 criptomoedas  
✅ Atualizar dados de CoinGecko  
✅ Deletar criptomoedas  
✅ Interface responsiva  
✅ Loading states  
✅ Error handling  

---

## Paleta de Cores

- **Primary**: `#667eea` - Roxo claro
- **Secondary**: `#764ba2` - Roxo escuro
- **Success**: `#10b981` - Verde
- **Error**: `#ef4444` - Vermelho
- **Background**: `#f5f7fa` - Cinza claro
- **Text**: `#333333` - Cinza escuro

---

## Configuração de CORS

O Vite proxy está configurado para redirecionar `/api` para `http://localhost:8080`

Se precisar mudar, edite `vite.config.js`:
```javascript
proxy: {
  '/api': {
    target: 'http://novo-url:porta',
    changeOrigin: true
  }
}
```

---

## Performance

- Component splitting para lazy loading
- State management centralizado
- Memoization onde necessário
- CSS modular para otimização

---

## Próximos Passos

1. Adicionar localStorage para cache
2. Implementar gráficos com Chart.js
3. Adicionar testes com Jest/React Testing Library
4. PWA (Progressive Web App)
5. Dark mode
6. Internacionalização (i18n)

---

## Links

- Backend: http://localhost:8080
- Frontend Dev: http://localhost:3000
- CoinGecko API: https://www.coingecko.com/en/api
