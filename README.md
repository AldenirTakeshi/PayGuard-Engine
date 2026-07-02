# PayGuard Engine

Monorepo do ecossistema **PayGuard Engine** — uma plataforma de processamento de pagamentos baseada em microsserviços, construída com Spring Boot 3 e Java 17.

O sistema modela transações financeiras sobre um **ledger de dupla entrada** (double-entry accounting), com liquidação assíncrona, verificação antifraude e notificações desacopladas via mensageria.

## Arquitetura

O projeto é um Maven multi-módulo composto por três microsserviços independentes que se comunicam de forma assíncrona através do RabbitMQ.

| Módulo | Porta | Responsabilidade |
| --- | --- | --- |
| `payguard-core-api` | `8080` | Liquidação e ledger — recebe transações, persiste lançamentos contábeis e calcula saldos. |
| `payguard-anti-fraud` | `8081` | Análise antifraude das transações. |
| `payguard-notification` | `8082` | Envio de notificações aos usuários. |

### Infraestrutura

| Serviço | Uso |
| --- | --- |
| MySQL 8.0 | Persistência das contas, lançamentos e transações. |
| Redis 7 | Cache e controle de idempotência de requisições. |
| RabbitMQ 3.11 | Mensageria entre os microsserviços. |
| Prometheus + Grafana | Métricas e observabilidade. |

## Stack

- **Java 17** / **Spring Boot 3.2.4**
- **Spring Cloud** 2023.0.1
- Spring Web, Spring Data JPA, Spring Data Redis, Bean Validation
- **Flyway** para versionamento do schema do banco
- **Lombok**
- **MySQL Connector/J**
- Testes com JUnit 5 e Mockito

## Pré-requisitos

- JDK 17+
- Maven 3.9+
- Docker e Docker Compose

## Como executar

### 1. Suba a infraestrutura

```bash
cp .env.example .env   # ajuste as variáveis conforme necessário
docker compose up -d
```

Isso inicializa MySQL, Redis, RabbitMQ, Prometheus e Grafana na rede `payguard-network`.

- RabbitMQ Management: http://localhost:15672 (`guest` / `guest`)
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (`admin` / `admin`)

### 2. Compile e rode os testes

```bash
mvn clean verify
```

### 3. Execute um microsserviço

```bash
mvn spring-boot:run -pl payguard-core-api
```

Substitua `payguard-core-api` por `payguard-anti-fraud` ou `payguard-notification` conforme necessário.

## Variáveis de ambiente

As variáveis são carregadas a partir do arquivo `.env` (veja `.env.example`):

| Variável | Padrão | Descrição |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/payguard_core` | URL de conexão do MySQL |
| `DB_USERNAME` / `DB_USER` | `payguard_user` | Usuário do banco |
| `DB_PASSWORD` | `payguard_password` | Senha do banco |
| `REDIS_HOST` / `REDIS_PORT` | `localhost` / `6379` | Conexão do Redis |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `localhost` / `5672` | Conexão do RabbitMQ |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `guest` / `guest` | Credenciais do RabbitMQ |

## API — `payguard-core-api`

Base URL: `http://localhost:8080`

### Criar transação

```
POST /api/v1/transactions
```

Registra uma transação inicial com status `PROCESSING` e retorna **202 Accepted** (liquidação assíncrona).

**Headers obrigatórios**

| Header | Descrição |
| --- | --- |
| `X-Idempotency-Key` | Chave de idempotência. A ausência resulta em **400 Bad Request**. |

**Corpo da requisição**

```json
{
  "accountOrigin": "uuid",
  "accountDestination": "uuid",
  "amount": 100.00,
  "currency": "BRL"
}
```

Todos os campos são obrigatórios; `amount` deve ser positivo.

**Resposta — 202 Accepted**

```json
{
  "id": "uuid",
  "status": "PROCESSING"
}
```

### Consultar saldo

```
GET /api/v1/accounts/{accountId}/balance
```

Retorna o saldo calculado a partir dos lançamentos do ledger. Retorna **404 Not Found** caso a conta não exista.

**Resposta — 200 OK**

```json
{
  "accountId": "uuid",
  "balance": 1500.0000
}
```

## Modelo de dados

O schema é gerido por migrations Flyway em `payguard-core-api/src/main/resources/db/migration`:

- **`accounts`** — contas de titulares.
- **`ledger_entries`** — lançamentos contábeis (`CREDIT` / `DEBIT`) que compõem o saldo. O saldo é calculado de forma stateless pelo `BalanceCalculator`.
- **`tb_transactions`** — transações com status (`PROCESSING`, `SUCCESS`, `FAILED`).

## Testes

```bash
mvn test
```

O módulo `payguard-core-api` inclui testes unitários do `LedgerService` (Mockito) e testes de integração de repositório (crédito, débito e saldo nulo).

## CI

O pipeline em `.github/workflows/ci.yml` executa `mvn clean verify` a cada push e pull request na branch `main`, provisionando um MySQL 8.0 como serviço.
