# E-commerce API

API de e-commerce para gerenciamento de **usuários, produtos, categorias, pedidos, carrinho e endereços**, com segurança JWT e documentação Swagger.

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot** 3.x
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Security + JWT**
- **Bean Validation**
- **Swagger/OpenAPI**
- **JUnit 5 + Mockito** (testes unitários e integração)

---

## ⚙️ Setup

### 1. **Clonar o repositório**
```bash
git clone <repo-url>
cd <repo-folder>
```

### 2. **Configurar banco de dados**
Editar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400
jwt.refresh-expiration=604800
```

### 3. **Rodar a aplicação**
```bash
./mvnw spring-boot:run
```

### 4. **Swagger UI**
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📚 Endpoints

### 🔐 **Usuários**

| Método | Endpoint | Descrição | Request/Response |
|--------|----------|-----------|------------------|
| `POST` | `/auth/register` | Registrar usuário | `{ "nome":"João Silva", "email":"joao@email.com", "senha":"12345678" }` |
| `POST` | `/auth/login` | Login | `{ "email":"joao@email.com", "senha":"12345678" }` → `{ "accessToken":"<JWT>", "refreshToken":"<JWT>" }` |
| `POST` | `/auth/refresh` | Renovar token | `{ "refreshToken":"<JWT>" }` → `{ "accessToken":"<JWT>" }` |
| `GET` | `/usuarios/perfil` | Obter perfil do usuário | → `{ "id":1, "nome":"João", "email":"joao@email.com" }` |
| `PUT` | `/usuarios/perfil` | Atualizar perfil | `{ "nome":"João Silva", "telefone":"(11) 99999-9999" }` |

### 🛍️ **Produtos**

| Método | Endpoint | Descrição | Request/Response |
|--------|----------|-----------|------------------|
| `GET` | `/produtos` | Listar produtos | → `[{"id":1,"nome":"Notebook","preco":2000.0}]` |
| `GET` | `/produtos/{id}` | Buscar produto | → `{ "id":1, "nome":"Notebook", "preco":2000.0 }` |
| `POST` | `/admin/produtos` | Criar produto | `{ "nome":"Notebook","descricao":"Intel i5","preco":2000.0,"estoque":10,"categoriaId":1 }` |
| `PUT` | `/admin/produtos/{id}` | Atualizar produto | `{ "nome":"Notebook Pro","preco":2500.0 }` |
| `DELETE` | `/admin/produtos/{id}` | Deletar produto | → `204 No Content` |

### 📂 **Categorias**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/categorias` | Listar categorias |
| `POST` | `/admin/categorias` | Criar categoria |
| `PUT` | `/admin/categorias/{id}` | Atualizar categoria |
| `DELETE` | `/admin/categorias/{id}` | Deletar categoria |

**Exemplo de requisição:**
```json
{
  "nome": "Eletrônicos",
  "descricao": "Produtos eletrônicos em geral"
}
```

### 🛒 **Carrinho**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/carrinho` | Listar itens do carrinho |
| `POST` | `/carrinho/itens` | Adicionar item ao carrinho |
| `PUT` | `/carrinho/itens/{id}` | Atualizar quantidade |
| `DELETE` | `/carrinho/itens/{id}` | Remover item |

**Exemplo de requisição (adicionar item):**
```json
{
  "produtoId": 1,
  "quantidade": 2
}
```

### 📦 **Pedidos**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/pedidos` | Listar meus pedidos |
| `GET` | `/pedidos/{id}` | Buscar pedido por ID |
| `POST` | `/pedidos` | Criar pedido |
| `PUT` | `/admin/pedidos/{id}/status` | Atualizar status (Admin) |

**Exemplo de requisição (criar pedido):**
```json
{
  "enderecoId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
```

### 📍 **Endereços**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/usuarios/enderecos` | Listar meus endereços |
| `POST` | `/usuarios/enderecos` | Adicionar endereço |
| `PUT` | `/usuarios/enderecos/{id}` | Atualizar endereço |
| `DELETE` | `/usuarios/enderecos/{id}` | Deletar endereço |

**Exemplo de requisição:**
```json
{
  "logradouro": "Rua das Flores, 123",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01000-000"
}
```

---

## 🔐 Autenticação

### **Login retorna JWT tokens:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "nome": "João Silva",
  "email": "joao@email.com",
  "role": "CLIENTE"
}
```

### **Para endpoints protegidos, enviar header:**
```
Authorization: Bearer <accessToken>
```

### **Renovar token quando expirar:**
```bash
POST /auth/refresh
{
  "refreshToken": "<refreshToken>"
}
```

---

## 🧪 Testes

### **Unitários**
```bash
./mvnw test
```
- Services testados com **JUnit 5 + Mockito**
- Cobertura mínima: **70%**

### **Integração**
```bash
./mvnw verify
```
- Controllers testados com **@SpringBootTest**
- Fluxos completos da API e autenticação

---

## 📋 Roles e Permissões

### **CLIENTE**
- ✅ Ver produtos e categorias
- ✅ Gerenciar carrinho
- ✅ Fazer pedidos
- ✅ Gerenciar perfil e endereços

### **ADMIN**
- ✅ Tudo do CLIENTE +
- ✅ Gerenciar produtos e categorias
- ✅ Gerenciar usuários
- ✅ Ver todos os pedidos
- ✅ Relatórios

### **VENDEDOR**
- ✅ Tudo do CLIENTE +
- ✅ Gerenciar produtos
- ✅ Ver pedidos

---

## 🛠️ Estrutura do Projeto

```
src/
├── main/java/com/ecommerce/
│   ├── config/          # Configurações (Security, Swagger)
│   ├── controller/      # Controllers REST
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Exception Handlers
│   ├── model/          # Entidades JPA
│   ├── repository/     # Repositories JPA
│   ├── security/       # JWT, UserDetails, Filters
│   ├── service/        # Regras de negócio
│   └── util/           # Utilitários
└── test/               # Testes unitários e integração
```

---

## 🚨 Status Codes

| Status | Descrição |
|--------|-----------|
| `200` | OK |
| `201` | Criado |
| `204` | Sem conteúdo |
| `400` | Requisição inválida |
| `401` | Não autenticado |
| `403` | Acesso negado |
| `404` | Não encontrado |
| `500` | Erro interno |

---

## 📝 Exemplos de Uso

### **1. Cadastrar e fazer login:**
```bash
# Cadastrar
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@email.com","senha":"123456"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@email.com","senha":"123456"}'
```

### **2. Listar produtos:**
```bash
curl -X GET http://localhost:8080/produtos
```

### **3. Adicionar item ao carrinho:**
```bash
curl -X POST http://localhost:8080/carrinho/itens \
  -H "Authorization: Bearer <seu-token>" \
  -H "Content-Type: application/json" \
  -d '{"produtoId":1,"quantidade":2}'
```

### **4. Criar pedido:**
```bash
curl -X POST http://localhost:8080/pedidos \
  -H "Authorization: Bearer <seu-token>" \
  -H "Content-Type: application/json" \
  -d '{"enderecoId":1}'
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

## 📞 Contato

- **Desenvolvedor**: Seu Nome
- **Email**: seu.email@exemplo.com
- **LinkedIn**: [seu-linkedin](https://linkedin.com/in/seu-perfil)