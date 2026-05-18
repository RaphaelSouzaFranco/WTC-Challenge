# WTC Challenge — Backend Java Spring Boot

Backend REST API para integração com o app Android WTC Challenge (Kotlin/Jetpack Compose).

---

## ⚙️ Stack

| Componente | Tecnologia |
|-----------|-----------|
| Framework | Spring Boot 3.2 |
| Linguagem | Java 17 |
| Banco de Dados | MongoDB |
| Autenticação | JWT (jjwt 0.12) |
| Tempo Real | Server-Sent Events (SSE) |
| Build | Maven |

---

## 🚀 Como Rodar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- MongoDB rodando localmente ou URI do Atlas

### 1. Configurar MongoDB

Edite `src/main/resources/application.properties`:

```properties
# Local
spring.data.mongodb.uri=mongodb://localhost:27017/wtcdb

# Ou Atlas
spring.data.mongodb.uri=mongodb+srv://<user>:<pass>@cluster.mongodb.net/wtcdb
```

### 2. Rodar o servidor

```bash
cd backend
mvn spring-boot:run
```

O servidor sobe em: `http://localhost:8080`

### 3. Conectar o Android

No `RetrofitInstance.kt`, troque a BASE_URL:

```kotlin
// Emulador Android → aponta para o host da máquina
private const val BASE_URL = "http://10.0.2.2:8080/"

// Dispositivo físico → use o IP local do servidor
private const val BASE_URL = "http://192.168.x.x:8080/"
```

---

## 📌 Endpoints REST

### 🔐 Autenticação — `/api/auth`

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/login` | Autentica e retorna JWT |
| POST | `/api/auth/logout` | Logout (client-side) |

**Login — Request:**
```json
POST /api/auth/login
{
  "email": "isabella@wtc.com",
  "senha": "senha123"
}
```

**Login — Response 200:**
```json
{
  "token": "eyJhbGci...",
  "tokenType": "Bearer",
  "operator": {
    "id": "...",
    "nome": "Isabella Rossi",
    "email": "isabella@wtc.com",
    "cargo": "Customer Support",
    "avatarUrl": null,
    "darkMode": true,
    "notas": ""
  }
}
```

> Todas as rotas seguintes exigem: `Authorization: Bearer {token}`

---

### 👥 Clientes — `/api/clients`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/clients` | Lista com filtros |
| GET | `/api/clients/{id}` | Busca por ID |
| POST | `/api/clients` | Cria cliente |
| PUT | `/api/clients/{id}` | Atualiza cliente |
| DELETE | `/api/clients/{id}` | Remove cliente |

**GET /api/clients — Query Params:**
```
?search=João      → busca nome/ramo
?status=Ativo     → filtra por status
?minScore=50      → score mínimo
?maxScore=90      → score máximo
?tag=VIP          → filtra por tag
```

**POST /api/clients — Request:**
```json
{
  "nome": "João Silva",
  "numero": "+5511999999999",
  "ramo": "Tecnologia",
  "status": "Lead",
  "tags": ["VIP", "Novo"],
  "score": 75,
  "operatorId": "..."
}
```

**Response ClientDTO:**
```json
{
  "id": "...",
  "nome": "João Silva",
  "numero": "+5511999999999",
  "ramo": "Tecnologia",
  "status": "Lead",
  "tags": ["VIP", "Novo"],
  "score": 75,
  "operatorId": "...",
  "createdAt": "2024-07-01T00:00:00Z",
  "updatedAt": "2024-07-01T00:00:00Z"
}
```

---

### 💬 Conversas — `/api/conversations`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/conversations?operatorId={id}&filter={Todos\|Clientes\|Grupos}` | Lista conversas |
| GET | `/api/conversations/{id}` | Busca por ID |
| POST | `/api/conversations` | Cria conversa |
| DELETE | `/api/conversations/{id}` | Remove conversa |
| GET | `/api/conversations/stream?operatorId={id}` | **SSE** — atualizações em tempo real |

**POST /api/conversations — Request:**
```json
{
  "clientId": "...",
  "operatorId": "..."
}
```

**Response ConversationDTO:**
```json
{
  "id": "...",
  "clientId": "...",
  "operatorId": "...",
  "client": { "...ClientDTO embutido..." },
  "lastMessage": "Olá, preciso de ajuda",
  "lastMessageAt": "2024-07-01T10:00:00Z",
  "unreadCount": 2,
  "isGroup": false,
  "createdAt": "2024-07-01T09:00:00Z"
}
```

---

### 📨 Mensagens — `/api/messages`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/messages/{conversationId}?page=0&size=30` | Histórico paginado |
| POST | `/api/messages/{conversationId}` | Envia texto |
| POST | `/api/messages/{conversationId}/media` | Envia mídia (multipart) |
| POST | `/api/messages/{conversationId}/deeplink` | Envia deeplink |
| PUT | `/api/messages/{messageId}/read` | Marca como lido |
| GET | `/api/messages/{conversationId}/stream` | **SSE** — stream em tempo real |

**POST texto — Request:**
```json
{
  "senderId": "operatorId",
  "senderType": "OPERATOR",
  "content": "Bom dia! Verificando seu pedido.",
  "type": "TEXT"
}
```

**POST mídia — multipart/form-data:**
```
file: [arquivo]
senderId: operatorId
senderType: OPERATOR
caption: "Comprovante em anexo" (opcional)
```

**POST deeplink — Request:**
```json
{
  "senderId": "operatorId",
  "senderType": "OPERATOR",
  "content": "Seu pedido está em trânsito. Rastreie aqui:",
  "deeplinkUrl": "wtcapp://track-order/12345",
  "deeplinkLabel": "Acompanhar pedido"
}
```

**Response MessageDTO:**
```json
{
  "id": "...",
  "conversationId": "...",
  "senderId": "...",
  "senderType": "OPERATOR",
  "type": "DEEPLINK",
  "content": "Seu pedido está em trânsito...",
  "mediaUrl": null,
  "mediaType": null,
  "deeplinkUrl": "wtcapp://track-order/12345",
  "deeplinkLabel": "Acompanhar pedido",
  "readAt": null,
  "createdAt": "2024-07-01T10:05:00Z"
}
```

---

### 📣 Campanhas — `/api/campaigns`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/campaigns?operatorId={id}&status={DRAFT\|SENT}` | Lista campanhas |
| GET | `/api/campaigns/{id}` | Busca por ID |
| POST | `/api/campaigns` | Cria campanha |
| POST | `/api/campaigns/{id}/media` | Upload de imagem (multipart) |
| POST | `/api/campaigns/{id}/send` | Envia campanha (DRAFT→SENT) |
| DELETE | `/api/campaigns/{id}` | Remove campanha |

**POST /api/campaigns — Request:**
```json
{
  "titulo": "Promoção de Verão",
  "mensagem": "Aproveite nossos descontos exclusivos!",
  "targetAudience": "Simple",
  "operatorId": "..."
}
```

---

### 👤 Operadores — `/api/operators`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/operators/{id}` | Busca perfil |
| PUT | `/api/operators/{id}` | Atualiza perfil |
| PUT | `/api/operators/{id}/avatar` | Upload de foto (multipart) |

**PUT /api/operators/{id} — Request:**
```json
{
  "nome": "Isabella Rossi",
  "cargo": "Customer Support",
  "notas": "Anotações pessoais...",
  "darkMode": true
}
```

---

## 📡 Tempo Real (SSE)

### Conectar ao stream de mensagens:
```
GET /api/messages/{conversationId}/stream
Accept: text/event-stream
```

Eventos recebidos:
```
event: new-message
data: {"id":"...","type":"TEXT","content":"Olá!","senderType":"CLIENT",...}
```

### Conectar ao stream de conversas:
```
GET /api/conversations/stream?operatorId={id}
Accept: text/event-stream
```

Eventos recebidos:
```
event: conversation-update
data: {"id":"...","lastMessage":"Nova mensagem","unreadCount":1,...}
```

---

## 🗂️ Estrutura de Pastas

```
backend/
├── pom.xml
└── src/main/java/com/wtc/backend/
    ├── WtcBackendApplication.java
    ├── config/
    │   ├── JwtUtils.java
    │   ├── JwtAuthFilter.java
    │   ├── SecurityConfig.java
    │   ├── WebConfig.java (CORS + uploads estáticos)
    │   ├── MongoConfig.java
    │   └── GlobalExceptionHandler.java
    ├── model/
    │   ├── Client.java
    │   ├── Operator.java
    │   ├── Conversation.java
    │   ├── Message.java
    │   └── Campaign.java
    ├── repository/
    │   ├── ClientRepository.java
    │   ├── OperatorRepository.java
    │   ├── ConversationRepository.java
    │   ├── MessageRepository.java
    │   └── CampaignRepository.java
    ├── dto/
    │   ├── LoginRequest.java / LoginResponse.java
    │   ├── OperatorDTO.java
    │   ├── ClientRequest.java / ClientDTO.java
    │   ├── ConversationDTO.java
    │   ├── MessageRequest.java / MessageDTO.java
    │   ├── DeeplinkMessageRequest.java
    │   ├── CampaignRequest.java / CampaignDTO.java
    ├── service/
    │   ├── AuthService.java
    │   ├── ClientService.java
    │   ├── ConversationService.java
    │   ├── MessageService.java
    │   ├── CampaignService.java
    │   ├── OperatorService.java
    │   ├── SseService.java
    │   └── FileStorageService.java
    └── controller/
        ├── AuthController.java
        ├── ClientController.java
        ├── ConversationController.java
        ├── MessageController.java
        ├── CampaignController.java
        └── OperatorController.java
```

---

## 🔧 Variáveis de Configuração

| Propriedade | Padrão | Descrição |
|------------|--------|-----------|
| `server.port` | `8080` | Porta do servidor |
| `spring.data.mongodb.uri` | `mongodb://localhost:27017/wtcdb` | URI do MongoDB |
| `app.jwt.secret` | (ver properties) | Chave secreta JWT — **mude em produção** |
| `app.jwt.expiration-ms` | `86400000` | Expiração do token (24h) |
| `app.upload.dir` | `uploads` | Diretório de uploads |
| `spring.servlet.multipart.max-file-size` | `50MB` | Tamanho máximo de upload |
