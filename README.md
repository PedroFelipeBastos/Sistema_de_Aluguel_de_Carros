---

# 🚗 Sistema de Aluguel de Carros 👨‍💻

> [!NOTE]
> Sistema web para gestão de pedidos e contratos de aluguel de automóveis, com avaliação financeira por agentes (empresas e bancos) e emissão de contratos associados a linhas de crédito.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este projeto foi desenvolvido na disciplina de <b>Desenvolvimento e Integração de Aplicações Web (DIAW)</b> do curso de <b>Engenharia de Software da PUC Minas</b>, sob orientação do <a href="https://github.com/joaopauloaramuni">Prof. Dr. João Paulo Carneiro Aramuni</a>. O sistema informatiza o ciclo completo de locação de veículos pela Internet: clientes cadastrados introduzem, consultam, modificam e cancelam pedidos de aluguel; agentes (empresas locadoras e bancos) avaliam financeiramente as solicitações e formalizam contratos de aluguel, definindo a titularidade do veículo e a eventual associação a contratos de crédito bancário.
      </div>
    </td>
    <td>
      <div>
        <img src="https://joaopauloaramuni.github.io/image/logo_ES_vertical.png" alt="Logo Engenharia de Software" width="120px"/>
      </div>
    </td>
  </tr> 
</table>

---

## 🚧 Status do Projeto

![Versão](https://img.shields.io/badge/Versão-v1.0.0-007ec6?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-25-007ec6?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.9-007ec6?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-007ec6?style=for-the-badge&logo=apachemaven&logoColor=white)
![React](https://img.shields.io/badge/React-19-007ec6?style=for-the-badge&logo=react&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-8-007ec6?style=for-the-badge&logo=vite&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/Supabase-PostgreSQL-007ec6?style=for-the-badge&logo=supabase&logoColor=white)

---

## 📚 Índice
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Variáveis de Ambiente](#-variáveis-de-ambiente)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Autores](#-autores)
- [Agradecimentos](#-agradecimentos)
- [Licença](#-licença)

---

## 📝 Sobre o Projeto

O **Sistema de Aluguel de Carros** apoia a gestão de aluguéis de automóveis através da Internet, subdividindo-se em dois subsistemas integrados:

1. **Subsistema de Gestão de Pedidos e Contratos (Back-end):** API RESTful em Spring Boot seguindo a arquitetura MVC/camadas.
2. **Subsistema de Construção Dinâmica das Páginas Web (Front-end):** Aplicação SPA desenvolvida com React + Vite.

### Regras de Negócio Atendidas
- **Autenticação obrigatória:** o acesso ao sistema exige cadastro prévio e login com usuário e senha.
- **Contratantes (Clientes):** armazenamento de RG, CPF, Nome, Endereço, Profissão, entidades empregadoras e respectivos rendimentos auferidos (máximo de 3).
- **Automóveis:** registro de matrícula, ano, marca, modelo, placa e proprietário atual.
- **Ciclo do Pedido:** clientes introduzem, modificam, consultam e cancelam pedidos; agentes (empresas e bancos) avaliam financeiramente os pedidos e emitem parecer positivo ou negativo.
- **Execução do Contrato:** pedidos aprovados podem ser convertidos em contratos de aluguel, registrando a propriedade do automóvel (cliente, empresa ou banco) e permitindo associar um contrato de crédito concedido por um banco agente.

---

## ✨ Funcionalidades Principais

- 🔐 **Autenticação e Autorização JWT:** Login obrigatório para clientes e agentes com controle de acesso por papel (`CLIENTE` e `AGENTE`).
- 👤 **CRUD Completo de Clientes:** Cadastro, consulta, atualização e exclusão de contratantes com até 3 rendimentos.
- 🚘 **Gestão da Frota de Automóveis:** Cadastro, edição, listagem e exclusão de veículos pelos agentes.
- 📋 **Gestão de Pedidos de Aluguel:** Criação, modificação, consulta de status e cancelamento de pedidos com verificação de conflito de datas.
- 🏦 **Avaliação Financeira e Contratos:** Parecer financeiro por agentes e formalização de contrato de aluguel com opção de contrato de crédito bancário.

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end
* **Biblioteca:** React 19 + React Router DOM 7
* **Build Tool:** Vite 8
* **Estilização:** CSS3 modular

### 🖥️ Back-end
* **Linguagem:** Java 25 (JDK 25)
* **Framework:** Spring Boot 4.0.9 (Spring Web MVC, Spring Data JPA, Validation)
* **Segurança:** Spring Security + OAuth2 Resource Server (JWT HS256 + BCrypt)
* **Banco de Dados:** Supabase (PostgreSQL) em produção e H2 Database em memória para desenvolvimento local
* **Build:** Apache Maven

---

## 🏗 Arquitetura

O sistema adota a arquitetura **MVC em camadas**, separando responsabilidades entre apresentação, controle, regras de negócio e persistência:

- **Controller (`com.aluguelcarros.controller`):** recebe as requisições HTTP REST, valida os DTOs de entrada e retorna os DTOs de resposta.
- **Service (`com.aluguelcarros.service`):** concentra as regras de negócio (limite de 3 rendimentos, validação de conflito de datas, transição de status dos pedidos e regras de propriedade/crédito).
- **Repository (`com.aluguelcarros.repository`):** interfaces Spring Data JPA para comunicação com o banco PostgreSQL/Supabase.
- **Model (`com.aluguelcarros.model`):** entidades JPA (`Usuario`, `Cliente`, `Agente`, `Rendimento`, `Automovel`, `Pedido`, `Contrato`, `ContratoCredito`).

---

## 🔧 Instalação e Execução

### Pré-requisitos
* **Java JDK:** 25
* **Apache Maven:** 3.9+
* **Node.js:** 20+ (com npm)

---

### 🔑 Variáveis de Ambiente

#### 1. Back-end (`backend/.env.example`)
Por padrão o perfil `dev` utiliza banco **H2 em memória** sem necessidade de configuração extra. Para conectar ao **Supabase (PostgreSQL)**, ative o perfil `prod` e defina:

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `PERFIL` | Perfil de execução (`dev` ou `prod`) | `prod` |
| `SERVER_PORT` | Porta do servidor Spring Boot | `8080` |
| `SUPABASE_DB_URL` | URL JDBC do Supabase PostgreSQL | `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres?sslmode=require` |
| `SUPABASE_DB_USUARIO` | Usuário do banco no Supabase | `postgres.seu_projeto` |
| `SUPABASE_DB_SENHA` | Senha do banco no Supabase | `sua_senha` |
| `JWT_SECRET` | Chave secreta (mín. 32 caracteres) para assinar tokens JWT | `chave-secreta-com-32-caracteres-min` |

#### 2. Front-end (`frontend/.env.example`)

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `VITE_API_URL` | URL base da API do Back-end | `http://localhost:8080/api` |

---

### ⚡ Como Executar a Aplicação

#### Terminal 1: Back-end (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run
```
🚀 *A API estará disponível em `http://localhost:8080`.*

**Agentes pré-cadastrados automaticamente na inicialização:**
- **Empresa:** usuário `empresa` | senha `empresa123`
- **Banco:** usuário `banco` | senha `banco123`

#### Terminal 2: Front-end (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
🎨 *A interface web estará disponível em `http://localhost:5173`.*

---

## 📂 Estrutura de Pastas

```text
.
├── README.md
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/aluguelcarros/
│       │   ├── config/          # Segurança JWT, CORS e carga inicial
│       │   ├── controller/      # Endpoints REST (MVC Controllers)
│       │   ├── dto/             # Objetos de transferência de dados
│       │   ├── exception/       # Exceções de domínio e handler global
│       │   ├── model/           # Entidades JPA e Enums
│       │   ├── repository/      # Repositórios Spring Data JPA
│       │   ├── security/        # Utilitário de usuário autenticado
│       │   └── service/         # Regras de negócio
│       └── resources/
│           ├── application.properties
│           ├── application-dev.properties
│           └── application-prod.properties
└── frontend/
    ├── package.json
    └── src/
        ├── components/          # Cabeçalho e navegação
        ├── pages/               # Telas (Login, Cadastro, Pedidos, Automóveis, Contratos, Clientes)
        └── services/            # Cliente HTTP (fetch) integrado à API
```

---

## 👥 Autores

| 👤 Nome | :octocat: GitHub |
|---------|-----------------|
| Pedro Felipe Bastos | [@PedroFelipeBastos](https://github.com/PedroFelipeBastos) |

---

## 🙏 Agradecimentos

* [**Engenharia de Software PUC Minas**](https://www.instagram.com/engsoftwarepucminas/)
* [**Prof. Dr. João Paulo Carneiro Aramuni**](https://github.com/joaopauloaramuni)

---

## 📄 Licença

Este projeto é distribuído sob a licença **MIT**.
