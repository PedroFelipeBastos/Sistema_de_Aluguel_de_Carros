# 🚗 Sistema de Aluguel de Carros 👨‍💻

> [!NOTE]
> Sistema web para apoio à gestão de aluguéis de automóveis, permitindo efetuar, cancelar e modificar pedidos, bem como avaliá-los financeiramente e executar contratos.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Projeto desenvolvido no Laboratório 02 da disciplina de <b>Desenvolvimento e Integração de Aplicações Web (DIAW)</b> do curso de <b>Engenharia de Software da PUC Minas</b>, lecionada pelo <a href="https://github.com/joaopauloaramuni">Prof. Dr. João Paulo Carneiro Aramuni</a>. O sistema apoia a gestão de aluguéis de automóveis pela Internet: clientes cadastrados podem introduzir, consultar, modificar e cancelar pedidos de aluguel, enquanto agentes (empresas e bancos) avaliam os pedidos do ponto de vista financeiro e executam os contratos de aluguel.
      </div>
    </td>
    <td>
      <div>
        <img src="https://joaopauloaramuni.github.io/image/logo_ES_vertical.png" alt="Logo do Projeto" width="120px"/>
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

O sistema foi subdividido em dois subsistemas: um para gestão de pedidos e contratos (back-end em Spring Boot) e outro para a construção dinâmica das páginas web (front-end em React + Vite).

- O acesso ao sistema exige cadastro prévio e autenticação (usuário e senha).
- Sobre os contratantes (clientes), são armazenados RG, CPF, Nome, Endereço, profissão, entidades empregadoras e respectivos rendimentos auferidos (máximo de 3).
- Sobre os automóveis, o sistema registra matrícula, ano, marca, modelo, placa e proprietário.
- Clientes podem introduzir, modificar, consultar e cancelar pedidos de aluguel.
- Agentes (empresas e bancos) podem modificar e avaliar pedidos financeiramente e, em caso de parecer positivo, executar o contrato registrando a propriedade do veículo (cliente, empresa ou banco) e eventual contrato de crédito bancário.

---

## ✨ Funcionalidades Principais

- **Autenticação:** Login de usuários (clientes e agentes) com token JWT.
- **CRUD de Clientes:** Cadastro, consulta, edição e exclusão de clientes e seus rendimentos (até 3).
- **CRUD de Automóveis:** Cadastro, listagem, atualização e remoção de veículos.
- **Pedidos de Aluguel:** Criação, modificação, cancelamento e consulta de status do pedido.
- **Avaliação e Contratos:** Parecer financeiro pelos agentes e execução do contrato de aluguel (com opção de contrato de crédito concedido por banco agente).

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end
* **Biblioteca:** React + React Router DOM
* **Build Tool:** Vite
* **Estilização:** CSS

### 🖥️ Back-end
* **Linguagem:** Java 25 (JDK 25)
* **Framework:** Spring Boot 4.0.x (Spring Web MVC, Spring Data JPA, Validation, Security)
* **Banco de Dados:** Supabase (PostgreSQL) / H2 (ambiente local de desenvolvimento)
* **Build e Dependências:** Apache Maven

---

## 🏗 Arquitetura

O back-end utiliza a arquitetura **MVC em camadas**:

- **Controller:** recebe as requisições HTTP da interface web e retorna os dados em JSON.
- **Service:** implementa as regras de negócio de clientes, automóveis, pedidos e contratos.
- **Repository:** comunicação com o banco de dados via Spring Data JPA / Hibernate.
- **Model / DTO:** entidades persistentes e objetos de transferência de dados.

---

## 🔧 Instalação e Execução

### Pré-requisitos
* **Java JDK:** 25
* **Apache Maven:** 3.9+
* **Node.js:** 20+

---

### 🔑 Variáveis de Ambiente

#### 1. Back-end (Spring Boot)
No perfil padrão (`dev`), o sistema roda com banco H2 em memória. Para conectar ao Supabase (PostgreSQL), utilize o perfil `prod` com as variáveis abaixo (veja `backend/.env.example`):

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `PERFIL` | Perfil ativo (`dev` ou `prod`) | `prod` |
| `SERVER_PORT` | Porta do Back-end | `8080` |
| `SUPABASE_DB_URL` | URL JDBC do Supabase | `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres?sslmode=require` |
| `SUPABASE_DB_USUARIO` | Usuário do banco | `postgres.seu_projeto` |
| `SUPABASE_DB_SENHA` | Senha do banco | `sua_senha` |
| `JWT_SECRET` | Chave secreta para assinatura do JWT | `chave-secreta-com-pelo-menos-32-caracteres` |

#### 2. Front-end (React, Vite)

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `VITE_API_URL` | URL da API do Back-end | `http://localhost:8080/api` |

---

### ⚡ Como Executar a Aplicação

#### Terminal 1: Back-end (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run
```
Back-end disponível em `http://localhost:8080`.

Usuários agentes criados na carga inicial para testes:
- Empresa: `empresa` / `empresa123`
- Banco: `banco` / `banco123`

#### Terminal 2: Front-end (React, Vite)
```bash
cd frontend
npm install
npm run dev
```
Front-end disponível em `http://localhost:5173`.

---

## 📂 Estrutura de Pastas

```text
.
├── README.md
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/aluguelcarros/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── exception/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── security/
│       │   └── service/
│       └── resources/
└── frontend/
    ├── package.json
    └── src/
        ├── components/
        ├── pages/
        └── services/
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

Este projeto é distribuído sob a **[Licença MIT](https://github.com/joaopauloaramuni/laboratorio-de-desenvolvimento-de-software/blob/main/LICENSE)**.
