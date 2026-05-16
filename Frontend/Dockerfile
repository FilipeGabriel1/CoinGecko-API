# Build stage
FROM node:18-alpine AS builder

WORKDIR /build

COPY package*.json .

RUN npm ci

COPY . .

RUN npm run build

# Runtime stage
FROM node:18-alpine

WORKDIR /app

# Instalar serve para rodar aplicação estática
RUN npm install -g serve

COPY --from=builder /build/dist ./dist

EXPOSE 3000

CMD ["serve", "-s", "dist", "-l", "3000"]
