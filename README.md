# Help Desk Enterprise

API REST para um sistema corporativo de Help Desk desenvolvida com **Java 21** e **Spring Boot**.

## 📝 Sobre

O **Help Desk Enterprise** é uma aplicação voltada para o gerenciamento e acompanhamento de chamados de suporte técnico (tickets), permitindo o ciclo de vida completo desde a abertura até o encerramento com controle de acesso por perfil (roles).

### Principais recursos:
- Autenticação de usuários via JWT
- Gerenciamento completo de tickets (criação, edição, atribuição e encerramento)
- Sistema de comentários em tickets (públicos e internos)
- Filtros avançados e paginação
- Controle de acesso baseado em papéis (Roles)
- Validação de dados com Bean Validation
- Tratamento global de exceções padronizado
- Logs de auditoria e controle de anexos

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x** (Spring Security, Spring Data JPA, Spring Web)
- **MySQL 8** (Banco de dados relacional)
- **Docker & Docker Compose** (Containerização do banco de dados)
- **JSON Web Token (JWT)** (Autenticação stateless)
- **MapStruct** (Mapeamento performático entre DTOs e Entidades)
- **Swagger / OpenAPI 3** (Documentação interativa da API)
- **JUnit 5 & Mockito** (Testes unitários e de integração)
- **Maven** (Gerenciamento de dependências e build)

---

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas padrão do ecossistema Spring:

```
[ Controller ]  ──►  [ Service ]  ──►  [ Repository ]  ──►  [ Database (MySQL) ]
```

- **DTOs**: Utilizados para isolar as entidades de domínio nas requisições e respostas.
- **Mappers (MapStruct)**: Mapeamento automatizado entre Entidades e DTOs.
- **Global Exception Handler**: Intercepta exceções da aplicação e retorna respostas JSON estruturadas (`ApiError`).
- **Security Filter**: Filtro JWT executado a cada requisição protegida para validar permissões do usuário.

---

## ⚙️ Configuração e Execução

### Pré-requisitos
- **Java 21** instalado
- **Docker** e **Docker Compose** instalados
- **Maven 3.8+** (ou utilizar o wrapper `./mvnw` incluso)

---

### 1. Clonar o repositório
```bash
git clone https://github.com/GabrielMontini30/Help_Desk_Enterprise_api.git
cd Help_Desk_Enterprise_api
```

---

### 2. Configurar as Variáveis de Ambiente (`.env`)
Crie um arquivo `.env` na raiz do projeto (mesmo diretório do `docker-compose.yml`). 

Você pode copiar o arquivo de exemplo:
```bash
cp .env.example .env
```

Preencha o arquivo `.env` com as suas credenciais e configurações:
```env
MYSQL_ROOT_PASSWORD="SuaSenhaRootAqui"
MYSQL_USERNAME="SeuUsuarioAqui"
MYSQL_PASSWORD="SuaSenhaUsuarioAqui"
MYSQL_URL="jdbc:mysql://localhost:3309/help_desk_enterprise"
JWT_SECRET="sua-chave-secreta-jwt-muito-longa-e-segura"
```

---

### 3. Subir o Banco de Dados com Docker Compose
O projeto está configurado para expor o MySQL na porta **`3309`** para evitar conflitos com instâncias locais do MySQL:

```bash
docker compose up -d
```

*Para verificar se o container está rodando perfeitamente:*
```bash
docker ps
```

---

### 4. Executar a Aplicação Spring Boot

#### Via Terminal:
```bash
./mvnw spring-boot:run
```

#### Via IntelliJ IDEA:
1. Abra o projeto no IntelliJ.
2. Certifique-se de ter o plugin **EnvFile** instalado e ativado na configuração de execução (`Run/Debug Configurations -> Enable EnvFile` e selecione o seu arquivo `.env`).
3. Execute a classe principal `api.App`.

A API iniciará no endereço: `http://localhost:8080`

---

## 📖 Documentação da API (Swagger UI)

Com a aplicação em execução, acesse a documentação interativa para testar os endpoints:

📌 **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 Executando os Testes

Para rodar a suíte completa de testes unitários e de integração (Controllers, Services, Repositories e JWT):

```bash
./mvnw test
```

---

## 👤 Autor

**Gabriel Montini**
- **GitHub:** [@GabrielMontini30](https://github.com/GabrielMontini30)
- **LinkedIn:** [Gabriel Montini](https://linkedin.com/in/gabriel-montini)
