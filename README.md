# CoinGecko API - Projeto Completo

## 📋 Visão Geral

Projeto acadêmico que integra **Backend Java/Spring Boot** com **Frontend React**, consumindo dados da **CoinGecko API**.

O projeto implementa:
- ✅ **6+ Padrões de Projeto**
- ✅ **5 Princípios SOLID**
- ✅ **Banco de Dados** (H2/PostgreSQL)
- ✅ **API REST** com Spring Boot
- ✅ **Frontend Responsivo** com React
- ✅ **State Management** com Context API
- ✅ **Integração com API Externa**

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────┐
│               Frontend (React)                       │
│  SearchBar • CryptoList • CryptoCard • Home         │
│  Context API • Services • Components                 │
│            (Port: 3000)                              │
└──────────────────┬──────────────────────────────────┘
                   │ HTTP/REST
                   ▼
┌─────────────────────────────────────────────────────┐
│            Backend (Spring Boot)                     │
│  Controller • Service • Repository                   │
│  Patterns: Repository, Service, Adapter, Factory     │
│            (Port: 8080)                              │
└──────────────────┬──────────────────────────────────┘
                   │ HTTP
                   ▼
        ┌──────────────────────────┐
        │   CoinGecko API          │
        │   Preços • Mercado       │
        │   Informações            │
        └──────────────────────────┘
                   │
                   ▼
        ┌──────────────────────────┐
        │   Banco de Dados         │
        │   (H2/PostgreSQL)        │
        │   Criptomoedas • Preços  │
        └──────────────────────────┘
```

---

## 📁 Estrutura do Projeto

```
.
├── Backend/                          # 🔧 Spring Boot
│   ├── src/main/java/com/coingecko/
│   │   ├── CoinGeckoApplication.java (App principal)
│   │   ├── api/                      (Controllers REST)
│   │   ├── model/                    (Entidades JPA)
│   │   ├── dto/                      (Transfer Objects)
│   │   ├── repository/               (Repository Pattern)
│   │   ├── service/                  (Service Pattern)
│   │   ├── service/impl/             (Implementações)
│   │   └── config/                   (Configuração/Singletons)
│   ├── src/main/resources/
│   │   └── application.yml           (Configuração)
│   ├── pom.xml                       (Maven)
│   └── README.md
│
├── Frontend/                         # ⚛️ React
│   ├── src/
│   │   ├── components/               (Componentes UI)
│   │   │   ├── SearchBar/
│   │   │   ├── CryptoCard/
│   │   │   └── CryptoList/
│   │   ├── context/                  (Context API)
│   │   │   └── CryptoContext.jsx     (State Management)
│   │   ├── pages/                    (Páginas)
│   │   │   └── Home.jsx
│   │   ├── services/                 (API Services)
│   │   │   └── apiService.js
│   │   ├── styles/                   (Estilos globais)
│   │   ├── App.jsx                   (Raiz da app)
│   │   └── main.jsx
│   ├── public/
│   ├── package.json                  (Dependencies)
│   ├── vite.config.js                (Vite config)
│   └── README.md
│
├── usr.java                          (Placeholder)
├── README.md                         (Este arquivo)
└── docker-compose.yml                (Opcional)
```

---

## 🎯 Padrões de Projeto Implementados

### Backend (6 padrões)

1. **Repository Pattern** → Abstração de dados
2. **Service Pattern** → Lógica de negócio
3. **Adapter Pattern** → Integração CoinGecko API
4. **Factory Pattern** → Criação de beans
5. **Singleton Pattern** → Instâncias únicas (Beans)
6. **Mapper/Builder Pattern** → Transformação DTO

### Frontend (5 padrões)

1. **Context Pattern** → Gerenciamento global
2. **Reducer Pattern** → State machine
3. **Command Pattern** → Ações do usuário
4. **Composition Pattern** → Componentes reutilizáveis
5. **Service Pattern** → Abstração HTTP

---

## 💡 Princípios SOLID

| Princípio | Implementação |
|-----------|-------------|
| **S**ingle Responsibility | Cada classe tem uma única responsabilidade |
| **O**pen/Closed | Aberto para extensão, fechado para modificação |
| **L**iskov Substitution | Subtipos substituem supertipos |
| **I**nterface Segregation | Interfaces específicas e pequenas |
| **D**ependency Inversion | Depender de abstrações, não implementações |

---

## 🚀 Como Executar

### Pré-requisitos
- **Java 17+**
- **Node.js 16+**
- **Maven** (backend)
- **npm** ou **yarn** (frontend)

### 1️⃣ Backend

```bash
cd Backend

# Compilar e rodar
mvn clean install
mvn spring-boot:run

# Ou com Maven Wrapper
./mvnw spring-boot:run
```

Backend rodará em: **http://localhost:8080**

Console H2: **http://localhost:8080/h2-console**

### 2️⃣ Frontend

```bash
cd Frontend

# Instalar dependências
npm install

# Rodar em desenvolvimento
npm run dev

# Build para produção
npm run build
```

Frontend rodará em: **http://localhost:3000**

---

## 📡 Endpoints da API

### Listagem
```bash
GET /api/v1/cryptocurrencies              # Todas
GET /api/v1/cryptocurrencies/top          # Top 10
GET /api/v1/cryptocurrencies/search       # Buscar por símbolo
```

### Busca Individual
```bash
GET /api/v1/cryptocurrencies/{id}         # Por ID
GET /api/v1/cryptocurrencies/symbol/{symbol}  # Por símbolo
```

### Operações
```bash
POST /api/v1/cryptocurrencies             # Criar
DELETE /api/v1/cryptocurrencies/{id}      # Deletar
PUT /api/v1/cryptocurrencies/{id}/update  # Atualizar de CoinGecko
```

---

## 📊 Exemplos de Requisições

### Buscar Bitcoin
```bash
curl http://localhost:8080/api/v1/cryptocurrencies/bitcoin
```

### Listar Top 10
```bash
curl http://localhost:8080/api/v1/cryptocurrencies/top
```

### Buscar por símbolo
```bash
curl "http://localhost:8080/api/v1/cryptocurrencies/search?symbol=BTC"
```

### Criar criptomoeda
```bash
curl -X POST http://localhost:8080/api/v1/cryptocurrencies \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ethereum",
    "symbol": "ETH",
    "name": "Ethereum",
    "description": "Plataforma de smart contracts"
  }'
```

---

## 🗄️ Banco de Dados

### Desenvolvimento (H2)
- URL: `jdbc:h2:mem:testdb`
- Usuário: `sa`
- Senha: (vazia)
- Console: http://localhost:8080/h2-console

### Produção (PostgreSQL)
Alterar em `Backend/src/main/resources/application.yml`

### Schema

**Tabela: cryptocurrencies**
| Campo | Tipo | PK |
|-------|------|-----|
| id | VARCHAR | ✓ |
| symbol | VARCHAR | UNIQUE |
| name | VARCHAR | |
| description | TEXT | |
| image_url | TEXT | |
| market_cap_rank | BIGINT | |
| last_updated | TIMESTAMP | |

**Tabela: cryptocurrency_prices**
| Campo | Tipo | FK |
|-------|------|-----|
| id | BIGINT | ✓ |
| cryptocurrency_id | VARCHAR | → cryptocurrencies.id |
| price_usd | DECIMAL | |
| price_eur | DECIMAL | |
| price_brl | DECIMAL | |
| volume_24h_usd | DECIMAL | |
| price_change_percentage_24h | DECIMAL | |
| market_cap | DECIMAL | |
| timestamp | TIMESTAMP | |

---

## 🔑 Funcionalidades

### Backend
- ✅ CRUD de criptomoedas
- ✅ Integração com CoinGecko API
- ✅ Busca e filtros
- ✅ Ranking por market cap
- ✅ Persistência em banco
- ✅ Logging estruturado
- ✅ Tratamento de erros

### Frontend
- ✅ Listagem de criptomoedas
- ✅ Busca em tempo real
- ✅ Preços em múltiplas moedas
- ✅ Indicadores de mudança
- ✅ Volume 24h
- ✅ Atualizar de CoinGecko
- ✅ CRUD local
- ✅ Interface responsiva
- ✅ Loading/Error states

---

## 🎨 Temas e Estilos

- **Primária**: #667eea (Roxo claro)
- **Secundária**: #764ba2 (Roxo escuro)
- **Sucesso**: #10b981 (Verde)
- **Erro**: #ef4444 (Vermelho)
- **Background**: #f5f7fa (Cinza claro)

---

## 🧪 Como Testar

### Backend
```bash
# Build e testes
mvn clean test

# Rodar coverage
mvn clean jacoco:jacoco
```

### Frontend
```bash
# Testes (quando implementados)
npm run test

# Coverage
npm run test:coverage
```

---

## 🔒 Configuração Segurança

### CORS
Frontend consegue acessar backend via proxy Vite

### Autenticação
- Atualmente sem autenticação
- Preparado para adicionar JWT

---

## 📝 Notas Importantes

1. **API CoinGecko** é rateada. Sem autenticação: ~10-50 requests/minuto
2. **H2 Console** disponível em desenvolvimento (`/h2-console`)
3. **CORS** habilitado para aceitar qualquer origem (dev)
4. **Banco em memória** (H2) é perdido ao reiniciar

---

## 🚀 Deployment

### Docker (Opcional)

```bash
# Build imagens
docker-compose build

# Rodar
docker-compose up
```

Será necessário criar `docker-compose.yml` e `Dockerfile`

---

## 📂 Próximos Passos

- [ ] Autenticação JWT
- [ ] Cache com Redis
- [ ] WebSocket em tempo real
- [ ] Gráficos com Chart.js
- [ ] Testes automatizados
- [ ] Documentação Swagger
- [ ] PWA
- [ ] Dark Mode
- [ ] Internacionalização

---

## 📞 Contato & Documentação

- **Backend README**: [Backend/README.md](./Backend/README.md)
- **Frontend README**: [Frontend/README.md](./Frontend/README.md)
- **CoinGecko API Docs**: https://www.coingecko.com/en/api

---

## 📄 Licença

Projeto acadêmico - Faculdade, 5º Período, Programação 4

---

## 👨‍💻 Stack Tecnológico

### Backend
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database
- PostgreSQL
- Lombok
- Apache Maven

### Frontend
- React 18.2.0
- Vite
- Axios
- React Context API
- CSS3
- npm

### Padrões & Princípios
- SOLID Principles
- Design Patterns (6 padrões)
- RESTful API
- Repository Pattern
- Service-Oriented Architecture

---

## ✨ Destacados

🎓 Projeto acadêmico com propósito educacional  
📚 Implementa conceitos avançados de engenharia de software  
🏗️ Arquitetura escalável e manutenível  
🔄 Integração back-end + front-end  
🌐 Consumo de API externa (CoinGecko)  
💾 Persistência em banco de dados  

---

**Desenvolvido com ❤️ para fins educacionais**
