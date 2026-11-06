# Histórias do Usuário

Abaixo estão descritas as Histórias do Usuário levantadas a partir dos requisitos do Sistema de Aluguel de Carros.

---

## HU01 - Cadastro de Cliente
- **Como:** usuário individual (cliente)
- **Eu quero:** realizar meu cadastro informando meus dados pessoais, profissão e até 3 rendimentos
- **Para que:** eu possa acessar o sistema e realizar pedidos de aluguel

**Critérios de Aceitação:**
1. O cliente deve informar nome, login, senha, RG, CPF, endereço e profissão.
2. O cliente pode cadastrar até no máximo 3 entidades empregadoras com seus respectivos rendimentos.
3. Não deve ser permitido cadastrar login, CPF ou RG duplicados.

---

## HU02 - Autenticação no Sistema (Login)
- **Como:** usuário cadastrado (cliente ou agente)
- **Eu quero:** me autenticar com meu usuário e senha
- **Para que:** eu tenha acesso às funcionalidades restritas ao meu perfil no sistema

**Critérios de Aceitação:**
1. O sistema só permite acesso às funcionalidades após login válido.
2. Credenciais inválidas devem retornar mensagem de erro sem liberar acesso.
3. O sistema deve identificar o papel do usuário (`CLIENTE` ou `AGENTE`) após o login.

---

## HU03 - Gerenciamento de Dados do Cliente (CRUD)
- **Como:** cliente (ou agente)
- **Eu quero:** consultar, atualizar e excluir o cadastro de cliente
- **Para que:** os dados de identificação, endereço, profissão e rendimentos permaneçam atualizados

**Critérios de Aceitação:**
1. O cliente pode visualizar, editar e excluir o próprio cadastro (caso não possua pedidos vinculados).
2. O agente pode listar, consultar, editar e excluir clientes cadastrados.
3. A edição deve respeitar o limite máximo de 3 rendimentos.

---

## HU04 - Gerenciamento de Automóveis
- **Como:** agente (empresa ou banco)
- **Eu quero:** cadastrar, consultar, editar e remover automóveis
- **Para que:** os veículos fiquem disponíveis para pedidos de aluguel

**Critérios de Aceitação:**
1. O cadastro do automóvel deve registrar matrícula, ano, marca, modelo e placa.
2. Clientes e agentes podem consultar os automóveis disponíveis.
3. Não deve ser possível excluir automóveis vinculados a pedidos existentes.

---

## HU05 - Introdução de Pedido de Aluguel
- **Como:** cliente autenticado
- **Eu quero:** criar um pedido de aluguel escolhendo o automóvel e o período desejado
- **Para que:** meu pedido seja submetido à avaliação financeira de um agente

**Critérios de Aceitação:**
1. O pedido é criado com status inicial `PENDENTE`.
2. A data de término deve ser posterior à data de início.
3. O automóvel não pode estar reservado (`APROVADO` ou `CONTRATADO`) no mesmo período.

---

## HU06 - Consulta, Modificação e Cancelamento de Pedido pelo Cliente
- **Como:** cliente autenticado
- **Eu quero:** consultar o status dos meus pedidos, modificá-los ou cancelá-los
- **Para que:** eu possa acompanhar o andamento ou desistir da locação

**Critérios de Aceitação:**
1. O cliente visualiza o status atual (`PENDENTE`, `APROVADO`, `REPROVADO`, `CANCELADO`, `CONTRATADO`) e o parecer financeiro.
2. Pedidos com status `PENDENTE` podem ter automóvel e datas modificados.
3. O cliente pode cancelar pedidos que estejam `PENDENTE` ou `APROVADO`.

---

## HU07 - Avaliação Financeira de Pedido pelo Agente
- **Como:** agente (empresa ou banco)
- **Eu quero:** modificar e avaliar financeiramente os pedidos de aluguel pendentes
- **Para que:** apenas pedidos com parecer financeiro positivo sigam para execução de contrato

**Critérios de Aceitação:**
1. O agente pode consultar todos os pedidos registrados no sistema.
2. Ao avaliar um pedido `PENDENTE`, o agente informa o parecer e define se foi aprovado (`APROVADO`) ou reprovado (`REPROVADO`).

---

## HU08 - Execução de Contrato de Aluguel e Contrato de Crédito
- **Como:** agente (empresa ou banco)
- **Eu quero:** executar o contrato de um pedido aprovado, definindo a propriedade do automóvel e eventual contrato de crédito
- **Para que:** o aluguel seja formalizado no sistema

**Critérios de Aceitação:**
1. Apenas pedidos com status `APROVADO` podem gerar contrato, passando para o status `CONTRATADO`.
2. O contrato registra se o automóvel ficará como propriedade do `CLIENTE`, `EMPRESA` ou `BANCO`.
3. O contrato pode estar associado a um contrato de crédito concedido por um banco agente (contendo banco, valor, taxa de juros e número de parcelas).
