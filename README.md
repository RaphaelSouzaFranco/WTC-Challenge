# WTC Challenge

Plataforma de atendimento e relacionamento com clientes composta por um app **Android (Kotlin + Jetpack Compose)** e um **backend REST em Java Spring Boot** com persistência em **MongoDB**. O operador de suporte gerencia clientes, conversa em tempo real e dispara campanhas de marketing direcionadas.

---

## Sumário

- [Arquitetura](#arquitetura)
- [Stack tecnológica](#stack-tecnológica)
- [Estrutura do repositório](#estrutura-do-repositório)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Funcionalidades](#funcionalidades)
- [Endpoints da API](#endpoints-da-api)
- [Fluxos de uso](#fluxos-de-uso)
- [Troubleshooting](#troubleshooting)

---

## Arquitetura

O sistema é dividido em três processos independentes que se comunicam por HTTP/JSON:

```
┌──────────────────┐   HTTP/JSON   ┌──────────────────┐   MongoDB driver   ┌──────────────────┐
│  App Android     │ ────────────► │ Backend Spring   │ ─────────────────► │     MongoDB      │
│  (Kotlin/Compose)│   :8080       │  (Java 21)       │  spring.data.mongo │  (local / Atlas) │
│                  │ ◄──────────── │                  │ ◄───────────────── │                  │
└──────────────────┘   Bearer JWT  └──────────────────┘                    └──────────────────┘
```

### Camadas do Android

| Camada | Pacote | Responsabilidade |
|---|---|---|
| **UI / Telas** | `Screens/` | Composables de tela (Login, Messages, Inbox, etc.) |
| **Composables reutilizáveis** | `composables/` | `BottomNavigationBar`, `ChatBottomBar`, `ConversationItem`, etc. |
| **Modelos de domínio** | `model/` | Data classes Kotlin espelhando os models do backend |
| **Rede** | `network/` | `RetrofitInstance`, `ApiService`, `SessionManager` |
| **DTOs de request** | `network/dto/` | DTOs específicos para enviar dados ao backend (login, register, etc.) |
| **Navegação** | `composables/Screen.kt` | Sealed class com as rotas do NavHost |

### Camadas do Backend

| Camada | Pacote | Responsabilidade |
|---|---|---|
| **Controllers** | `controller/` | Endpoints REST (`@RestController`) |
| **Services** | `service/` | Regra de negócio |
| **Repositories** | `repository/` | Spring Data MongoDB |
| **Models** | `model/` | Entidades persistidas (`@Document`) |
| **DTOs** | `dto/` | Contratos de request e response |
| **Config** | `config/` | JWT, Security, CORS, Mongo |

### Autenticação

- O backend emite **JWT** no login (`POST /api/auth/login`)
- O Android guarda o token em memória (`SessionManager`)
- Um interceptor do OkHttp injeta `Authorization: Bearer <token>` em todas as requisições subsequentes
- Endpoints públicos: `/api/auth/login`, `/api/auth/register`, `/api/auth/logout`

---

## Stack tecnológica

### Android
- **Kotlin** + **Jetpack Compose** (Material 3)
- **Retrofit 2.9** + **Gson** para HTTP
- **OkHttp** com Logging Interceptor e Auth Interceptor
- **Navigation Compose** para roteamento
- **Coroutines** para chamadas assíncronas

### Backend
- **Java 21**
- **Spring Boot 3.2.5** (Web, Validation, Security, Data MongoDB, DevTools)
- **Spring Security** com BCrypt + JWT (`jjwt 0.12.5`)
- **Server-Sent Events** para stream de mensagens/conversas

### Banco
- **MongoDB** (local ou Atlas)

---

## Estrutura do repositório

```
WTCChallenge/
├── app/                                # Módulo Android
│   └── src/main/java/com/example/wtcchallenge/
│       ├── MainActivity.kt             # Ponto de entrada + NavHost
│       ├── Screens/                    # 10 telas
│       │   ├── LoginScreen.kt
│       │   ├── RegisterScreen.kt
│       │   ├── MessagesScreen.kt
│       │   ├── InboxScreen.kt          # Chat com cliente
│       │   ├── SupportScreen.kt        # Chat por conversationId
│       │   ├── ClientListScreen.kt     # CRUD de clientes
│       │   ├── ClientProfileScreen.kt
│       │   ├── ClientTimelineScreen.kt
│       │   ├── CampaignScreen.kt       # Criação e envio de campanhas
│       │   ├── SegmentScreen.kt
│       │   └── ProfileScreen.kt
│       ├── composables/                # UI compartilhada
│       │   ├── ScrollbarModifier.kt
│       │   ├── ChatComponentes.kt
│       │   ├── ConversationItem.kt
│       │   └── ...
│       ├── model/                      # Espelha backend/model/
│       │   ├── Client.kt
│       │   ├── Conversation.kt
│       │   ├── Message.kt
│       │   ├── Campaign.kt
│       │   ├── Operator.kt
│       │   ├── Segment.kt
│       │   └── TimelineEvent.kt
│       └── network/
│           ├── RetrofitInstance.kt
│           ├── ApiService.kt
│           ├── SessionManager.kt
│           └── dto/                    # Apenas DTOs de request
│               ├── LoginRequestDto.kt
│               ├── RegisterRequestDto.kt
│               ├── MessageRequestDto.kt
│               ├── CampaignRequestDto.kt
│               ├── ClientRequestDto.kt
│               └── ...
│
├── backend/                            # Spring Boot
│   └── src/main/
│       ├── java/com/wtc/backend/
│       │   ├── WtcBackendApplication.java
│       │   ├── controller/             # 8 controllers REST
│       │   ├── service/                # Regra de negócio
│       │   ├── repository/             # Spring Data Mongo
│       │   ├── model/                  # @Document
│       │   ├── dto/                    # Request/Response DTOs
│       │   └── config/                 # JWT, Security, Mongo, CORS
│       └── resources/
│           └── application.properties  # URI do Mongo, JWT secret, etc.
│
└── README.md
```

---

## Pré-requisitos

| Componente | Versão mínima |
|---|---|
| **JDK** | 21 |
| **Android Studio** | Hedgehog (2023.1) ou superior |
| **MongoDB** | 6.x local OU conta MongoDB Atlas |
| **Android SDK** | API 26+ (target API 36) |

---

## Como executar

### 1. Backend (Spring Boot)

#### a) Configurar o MongoDB

Edite `backend/src/main/resources/application.properties` conforme seu ambiente:

**MongoDB local:**
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/wtcdb
spring.data.mongodb.database=wtcdb
```

**MongoDB Atlas:**
```properties
spring.data.mongodb.uri=mongodb+srv://<usuario>:<senha>@cluster.mongodb.net/wtcdb?retryWrites=true&w=majority
spring.data.mongodb.database=wtcdb
```

> No Atlas, libere seu IP em **Network Access → IP Access List** (em dev pode usar `0.0.0.0/0`).

#### b) Subir o servidor

Na pasta `backend/`:

```bash
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

Ou abra o projeto no **IntelliJ IDEA** e rode `WtcBackendApplication.java`.

Quando aparecer no console:
```
Tomcat started on port 8080
Started WtcBackendApplication in X.XXX seconds
```
o backend está pronto.

### 2. App Android

1. Abra a raiz do repositório no **Android Studio**
2. Aguarde o Gradle sincronizar
3. Conecte um dispositivo físico ou inicie um emulador
4. Clique em **Run ▶️** (toolbar)

> **Importante:** o app aponta para `http://10.0.2.2:8080/` (definido em `RetrofitInstance.kt`), que é o `localhost` da máquina vista pelo emulador. Para rodar em **dispositivo físico**, troque pela URL/IP da máquina onde o backend está rodando.

### 3. Criar conta e usar

Como a base começa vazia, cadastre seu primeiro operador na própria tela:

1. Tela de Login → clique em **Cadastre-se**
2. Preencha nome, e-mail e senha (mín. 6 caracteres) → **Cadastrar**
3. Volta para a tela de login → entre com as credenciais

---

## Funcionalidades

### Autenticação e perfil
- Cadastro de operadores (`/api/auth/register`)
- Login com JWT (`/api/auth/login`)
- Refresh automático de token expirado (interceptor do OkHttp)
- Tela de perfil com nome, e-mail, cargo e modo escuro persistido no banco

### Clientes
- Listagem com busca local por nome, ramo, **status, tags e score**
- Cadastro de cliente via diálogo (nome, número, ramo, status, tags, score)
- Cada cliente tem timeline de eventos e tela de perfil

### Conversas e mensagens
- Lista de conversas do operador com filtro Todos / Clientes / Grupos
- Clique em uma conversa abre a `InboxScreen` daquele cliente
- Chat em tempo real com balões (operador à direita, cliente à esquerda)
- Mensagens em ordem cronológica (mais antigas no topo)
- Scrollbar customizada animada para conversas longas
- Endpoint SSE para stream de novas mensagens (preparado no backend)

### Campanhas
- Criação de campanhas (título + mensagem + público-alvo + imagem opcional)
- Segmentos de clientes
- Agendamento de envio (em horas a partir de agora)
- **Distribuição multi-cliente:** seleção por checkbox de quais clientes recebem a campanha, com entrega como mensagem na conversa de cada um
- Teste A/B (variantes de título/mensagem)

---

## Endpoints da API

Base URL: `http://localhost:8080`

### Auth
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/auth/register` | Cadastra novo operador |
| `POST` | `/api/auth/login` | Login → retorna `{ token, operator }` |
| `POST` | `/api/auth/refresh` | Renova token via refresh token |
| `POST` | `/api/auth/logout` | Logout (stateless) |

### Clientes
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/clients` | Lista (filtros: `search`, `status`, `minScore`, `maxScore`, `tag`) |
| `GET` | `/api/clients/{id}` | Busca por ID |
| `POST` | `/api/clients` | Cria cliente |
| `GET` | `/api/clients/{id}/timeline` | Eventos do cliente |

### Conversas
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/conversations?operatorId=&filter=` | Lista por operador |
| `POST` | `/api/conversations` | Cria ou retorna conversa existente (idempotente) |
| `GET` | `/api/conversations/stream` | SSE de atualizações |

### Mensagens
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/messages/{conversationId}` | Histórico paginado |
| `POST` | `/api/messages/{conversationId}` | Envia mensagem de texto |
| `POST` | `/api/messages/{conversationId}/media` | Envia mídia (multipart) |
| `GET` | `/api/messages/{conversationId}/stream` | SSE de novas mensagens |

### Inbox (visão por cliente)
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/inbox/{customerId}` | Conversas de um cliente |
| `GET` | `/api/inbox/{customerId}/messages` | Mensagens (todas as conversas do cliente) |

### Campanhas
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/campaigns?operatorId=` | Lista por operador |
| `POST` | `/api/campaigns` | Cria campanha (status DRAFT) |
| `POST` | `/api/campaigns/{id}/send` | Marca como SENT |
| `POST` | `/api/campaigns/{id}/schedule` | Agenda envio |
| `POST` | `/api/campaigns/{id}/abtest` | Cria variante B |
| `POST` | `/api/campaigns/{id}/media` | Upload de imagem |

### Operadores
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/operators/{id}` | Dados do operador |
| `PUT` | `/api/operators/{id}` | Atualiza nome / notas / modo escuro |

### Segmentos
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/segments?operatorId=` | Lista |
| `POST` | `/api/segments` | Cria |
| `PUT` | `/api/segments/{id}` | Atualiza |
| `DELETE` | `/api/segments/{id}` | Remove |

---

## Fluxos de uso

### Cadastrar e logar
1. **LoginScreen** → toque em **Cadastre-se**
2. **RegisterScreen** → nome + e-mail + senha → **Cadastrar**
3. Volta para login → credenciais → **Entrar**
4. JWT salvo no `SessionManager`, navega para **MessagesScreen**

### Criar um cliente
1. Aba inferior **Clientes** → ícone **+** no topo
2. Preencha nome, número (obrigatórios), ramo, status, tags, score
3. **Salvar** → `POST /api/clients` → lista atualiza

### Conversar com um cliente
- **Via Clientes:** ClientListScreen → ícone de inbox no cliente → InboxScreen
- **Via Conversas:** MessagesScreen → toque na conversa → InboxScreen
- Digite no campo de baixo → **Enviar** → mensagem aparece à direita imediatamente

### Disparar campanha para múltiplos clientes
1. Aba inferior **Campanhas**
2. Preencha título + mensagem + público + (opcional) imagem
3. **Send Now** → abre **diálogo de destinatários**
4. Marque os clientes (ou **Selecionar todos**)
5. **Enviar (N)** → para cada selecionado: cria/usa a conversa e envia a campanha como mensagem
6. Feedback: `"Campanha entregue para X de Y cliente(s)"`
7. Mensagens aparecem nas inboxes dos clientes selecionados com prefixo 📢

### Agendar campanha
- Igual ao anterior, mas preencha **Horas a partir de agora** antes de clicar em **Send Now**
- Nesse caso, o diálogo de destinatários **não abre** — a campanha fica com status `SCHEDULED`

---

## Troubleshooting

### "Não foi possível conectar ao servidor" ao logar/cadastrar
- O backend não está rodando, ou está em outra porta
- Verifique no console se aparece `Tomcat started on port 8080`
- Teste com `curl http://localhost:8080/api/clients` — qualquer resposta (mesmo 401) prova que o backend está vivo

### Backend caiu com `MongoSocketException` / `MongoTimeoutException`
- A connection string em `application.properties` está errada, ou
- O IP da sua máquina não está na whitelist do Atlas (**Network Access**)
- Senha com caracteres especiais precisa de URL-encoding

### App no celular físico não conecta
- `10.0.2.2` só funciona no emulador Android
- Troque `BASE_URL` em `network/RetrofitInstance.kt` pelo **IP da máquina** onde o backend está rodando (ex: `http://192.168.0.10:8080/`)
- Garanta que o firewall do Windows está liberando a porta 8080

### Mensagens aparecem na ordem errada
- Já corrigido: o Android ordena por `createdAt` ascendente após receber do backend

### Login retorna "E-mail ou senha inválidos" para credenciais existentes
- Confirme se está logando com o e-mail exato que foi cadastrado (case-sensitive no Mongo)
- Verifique no Mongo se o documento do operador realmente existe na collection `operators`

---

## Licença

Projeto acadêmico desenvolvido como desafio WTC.
