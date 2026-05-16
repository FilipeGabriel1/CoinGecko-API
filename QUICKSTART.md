# 🚀 INÍCIO RÁPIDO

## ⚡ Resumo do Projeto

```
Frontend (React)          Backend (Spring Boot)     CoinGecko API
┌──────────────────┐      ┌────────────────────┐    ┌──────────────┐
│ localhost:3000   │ ◄───►│ localhost:8080     │ ◄──►│ api.coingecko│
│                  │      │                    │    │              │
│ - SearchBar      │      │ - Controllers      │    │ Preços       │
│ - CryptoList     │      │ - Services         │    │ Volumes      │
│ - CryptoCard     │      │ - Repositories     │    │ Market Cap   │
│ - React Context  │      │ - Config           │    │              │
└──────────────────┘      └────────────────────┘    └──────────────┘
```

---

## 📦 Pré-requisitos

```bash
# Verificar versões (substitua 'java' por 'echo' se não tiver instalado)
java -version           # Java 17+
mvn -version           # Maven 3.8+
node -version          # Node 16+
npm -version           # npm 8+
```

Se não tiver, instale:
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven](https://maven.apache.org/download.cgi)
- [Node.js](https://nodejs.org/)

---

## 🏃 Execução Rápida (5 minutos)

### 1️⃣ Terminal 1 - Backend

```bash
cd Backend
mvn clean install
mvn spring-boot:run
```

✅ Backend pronto em http://localhost:8080

### 2️⃣ Terminal 2 - Frontend

```bash
cd Frontend
npm install
npm run dev
```

✅ Frontend pronto em http://localhost:3000

### 3️⃣ Abra no navegador

```
http://localhost:3000
```

---

## 📚 Estrutura de Arquivos Importantes

```
Backend/
├── src/main/java/com/coingecko/
│   ├── CoinGeckoApplication.java    ← Iniciar
│   ├── api/
│   │   └── CryptoCurrencyController.java
│   ├── model/
│   │   ├── Cryptocurrency.java
│   │   └── CryptoCurrencyPrice.java
│   ├── service/
│   │   ├── CryptoCurrencyService.java (interface)
│   │   └── impl/
│   │       └── CryptoCurrencyServiceImpl.java
│   └── repository/
│       └── CryptocurrencyRepository.java
├── src/main/resources/
│   └── application.yml              ← Config
└── pom.xml                          ← Dependencies

Frontend/
├── src/
│   ├── App.jsx                      ← Raiz
│   ├── pages/
│   │   └── Home.jsx
│   ├── components/
│   │   ├── SearchBar/
│   │   ├── CryptoCard/
│   │   └── CryptoList/
│   ├── context/
│   │   └── CryptoContext.jsx        ← State global
│   ├── services/
│   │   └── apiService.js            ← HTTP Client
│   └── styles/
├── vite.config.js                   ← Config
└── package.json                     ← Dependencies
```

---

## 🔗 URLs Importantes

| Serviço | URL | Descrição |
|---------|-----|-----------|
| Frontend | http://localhost:3000 | Aplicação React |
| Backend | http://localhost:8080 | API Spring |
| H2 Console | http://localhost:8080/h2-console | DB (dev) |
| CoinGecko API | https://api.coingecko.com/api/v3 | API externa |

---

## 🧪 Testar Endpoints

### Curl - Listar Top 10

```bash
curl http://localhost:8080/api/v1/cryptocurrencies/top
```

### Curl - Buscar Bitcoin

```bash
curl http://localhost:8080/api/v1/cryptocurrencies/bitcoin
```

### Curl - Buscar por símbolo

```bash
curl "http://localhost:8080/api/v1/cryptocurrencies/search?symbol=BTC"
```

---

## 🐳 Docker (Alternativa)

```bash
# Build e rodar containers
docker-compose up --build

# Parar
docker-compose down
```

Então acesse http://localhost:3000

---

## 📂 Passos Detalhados

### Backend Step-by-step

```bash
# 1. Navegue para Backend
cd Backend

# 2. Build do projeto (compila Java, baixa deps)
mvn clean install
# Tempo: ~2-3 minutos primeira vez

# 3. Execute
mvn spring-boot:run
# Deve ver: "Started CoinGeckoApplication in X seconds"

# 4. Teste
curl http://localhost:8080/api/v1/cryptocurrencies
# Deve retornar []  (vazio no início)
```

### Frontend Step-by-step

```bash
# 1. Em outro terminal, navegue para Frontend
cd Frontend

# 2. Instale dependências
npm install
# Tempo: ~1-2 minutos

# 3. Execute em desenvolvimento
npm run dev
# Deve ver: "Local: http://localhost:3000"

# 4. Navegador abre automaticamente em http://localhost:3000
```

---

## 🛠️ Troubleshooting

### Backend não inicia

```bash
# Erro: "Port 8080 already in use"
# Solução: Mude a porta em Backend/src/main/resources/application.yml

# Erro: "Java not found"
# Solução: Instale Java 17+ e adicione ao PATH
```

### Frontend não inicia

```bash
# Erro: "npm: command not found"
# Solução: Instale Node.js (inclui npm)

# Erro: "Port 3000 already in use"
# Solução: npm run dev -- --port 3001
```

### Conexão entre Front/Back não funciona

```bash
# Verifique se backend está rodando:
curl http://localhost:8080

# Se não funcionar, verifique proxy em vite.config.js
# Deve apontar para http://localhost:8080
```

---

## 💻 Comandos Úteis

### Backend

```bash
# Rodar testes
mvn test

# Build sem executar
mvn clean package

# Limpar cache Maven
mvn clean

# Compilar apenas
mvn compile
```

### Frontend

```bash
# Build para produção
npm run build

# Ver build
npm run preview

# Lint código
npm run lint

# Deletar node_modules e reinstalar
rm -rf node_modules && npm install
```

---

## 📊 Padrões Vistos em Ação

Ao usar a aplicação, você verá na prática:

```
# Buscar criptomoeda (Repository Pattern)
1. Clique em "Todas" ou "Top 10" no Frontend
2. Frontend chamada apiService.getAllCryptos()
3. Service HTTP abstrai requisição
4. Backend CryptoCurrencyController recebe
5. Service implementação coordena lógica
6. Repository busca no banco
7. Mapper converte Entidade → DTO
8. Response volta com JSON

# Atualizar de CoinGecko (Adapter Pattern)
1. Clique em "🔄" (botão update)
2. Service chamaAdapter CoinGeckoApiServiceImpl
3. Adapter consome https://api.coingecko.com/...
4. Parser transforma resposta genérica em DTO
5. Service persistência no banco
6. Frontend atualiza UI com dados novos
```

---

## 🎯 Próximas Ações

Após ter tudo rodando:

1. **Explore as classes** para entender os padrões
2. **Leia PATTERNS.md** para saber cada padrão
3. **Leia SOLID.md** para entender princípios
4. **Modifique código** para aprender
5. **Adicione uma feature** nova

---

## 📝 Diário de Desenvolvimento

```bash
# 1. Backend rodando
npm run dev    # Frontend

# 2. Abrir browser
http://localhost:3000

# 3. Debugar
- Abrir DevTools (F12)
- Ver rede/requisições
- Network tab mostra chamadas para http://localhost:8080

# 4. Banco de dados
http://localhost:8080/h2-console
- Visualizar tabelas
- Ver dados salvos
```

---

## 🎓 Dicas de Aprendizado

1. **Entenda os padrões primeiro** → Leia PATTERNS.md
2. **Veja SOLID em ação** → Leia SOLID.md
3. **Rastreie uma requisição** → Use DevTools
4. **Modifique uma classe** → Veja o efeito
5. **Tente quebrar** → Aprenda os limites

---

## ✅ Checklist Rápido

- [ ] Java 17 instalado
- [ ] Node.js instalado
- [ ] Backend iniciou (http://localhost:8080)
- [ ] Frontend iniciou (http://localhost:3000)
- [ ] Consegue listar criptomoedas
- [ ] Consegue buscar
- [ ] Consegue atualizar de CoinGecko
- [ ] H2 Console acessível

---

## 📞 Support

Se algo não funcionar:

1. Verificar versões: `java -version`, `mvn -version`, `node -version`
2. Limpar build: `mvn clean`, `rm -rf node_modules`
3. Reinstalar: `mvn install`, `npm install`
4. Verificar portas: `lsof -i :8080`, `lsof -i :3000`
5. Ler logs no terminal

---

