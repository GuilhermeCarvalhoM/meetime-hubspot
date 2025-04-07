# HubConnect Meetime - Integração com HubSpot

Este projeto é uma aplicação Java com Spring Boot que realiza integração com a API do HubSpot utilizando OAuth 2.0, possibilitando:

- Autenticação segura via OAuth.
- Criação de contatos no CRM HubSpot.
- Recebimento de eventos via Webhook.
- Controle de taxa de requisições com Rate Limiter.

🚀 Tecnologias

- Java 17+
- Spring Boot
- Spring Web
- RestTemplate
- ngrok (para testes com Webhook)
- HubSpot Developer Account


📦 Pré-requisitos

- JDK 17+
- Maven ou Gradle
- Conta de desenvolvedor no [HubSpot Developer Portal](https://developers.hubspot.com/)
- ngrok (para testes com Webhook): [Download](https://ngrok.com/download)


🔧 Configuração

1. **Clone o projeto:**

```bash
git clone https://github.com/GuilhermeCarvalhoM/meetime-hubspot.git
cd hubconnect-meetime
```

2. Configure o `application.properties`:

Crie um arquivo em `src/main/resources/application.properties`:

```properties
server.port=8080

hubspot.client-id=SEU_CLIENT_ID
hubspot.client-secret=SEU_CLIENT_SECRET
hubspot.redirect-uri=http://localhost:8080/oauth/callback
```

---

▶️ Executando o Projeto

```bash
./mvnw spring-boot:run
```

ou

```bash
./gradlew bootRun
```

---

🔐 Fluxo OAuth

1. Acesse:  
   `http://localhost:8080/oauth/url`

2. Clique no link de autorização que será retornado.

3. Após aceitar os escopos, o HubSpot redirecionará para:  
   `http://localhost:8080/oauth/callback?code=...`

4. O token de acesso será armazenado temporariamente na aplicação.

---

👤 Criando um Contato

**Endpoint:**  
`POST http://localhost:8080/contacts`

**Payload:**
```json
{
  "email": "teste@example.com",
  "firstName": "João",
  "lastName": "Silva"
}
```

**Resposta esperada:**  
201 Created com os dados do novo contato do HubSpot.

---

🔔 Webhook

1. **Rode o ngrok para expor a porta 8080:**

```bash
ngrok http 8080
```

2. Pegue a URL pública gerada e registre no HubSpot como Webhook:

   - Exemplo:  
     `https://seu-subdominio.ngrok-free.app/webhook/contact`

3. **HubSpot enviará eventos nesse formato:**

```json
[
  {
    "eventId": 12345,
    "subscriptionType": "contact.creation",
    "objectId": 67890
  }
]
```

---

📄 Estrutura de Código

- `AuthController`: Gerencia autenticação e troca de token.
- `ContactController`: Criação de contatos.
- `WebhookController`: Recebe eventos de contato via Webhook.
- `HubspotService`: Camada de integração com a API do HubSpot.
- `RateLimiter`: Limita requisições para evitar sobrecarga.

---

✅ Testes

Você pode testar com ferramentas como:

- Postman
- Insomnia
- curl

Exemplo com `curl`:

```bash
curl -X POST http://localhost:8080/contacts \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "firstName": "João",
    "lastName": "Silva"
  }'
```

---

📌 Observações

- O token de acesso é armazenado em memória (classe `AuthController`). Em produção, é recomendável usar cache (Redis) ou banco de dados.
- O rate limiter atual é simples, apenas para controle local. Pode ser melhorado com bucket/token ou Redis para ambientes distribuídos.
- ngrok é útil apenas para desenvolvimento. Em produção, use HTTPS com domínio válido.

👨‍💻 Autor

Desenvolvido por Guilherme Carvalho
Contato:guilherme.carvalhoG@Outlook.com

