const URL_API = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const CHAVE_SESSAO = 'sessao_aluguel_carros';

export function obterSessao() {
  const salvo = localStorage.getItem(CHAVE_SESSAO);
  return salvo ? JSON.parse(salvo) : null;
}

export function salvarSessao(dados) {
  localStorage.setItem(CHAVE_SESSAO, JSON.stringify(dados));
}

export function limparSessao() {
  localStorage.removeItem(CHAVE_SESSAO);
}

async function requisitar(caminho, opcoes = {}) {
  const sessao = obterSessao();
  const cabecalhos = {
    'Content-Type': 'application/json',
    ...(sessao?.token ? { Authorization: `Bearer ${sessao.token}` } : {}),
    ...opcoes.headers,
  };

  const resposta = await fetch(`${URL_API}${caminho}`, {
    ...opcoes,
    headers: cabecalhos,
  });

  if (resposta.status === 204) {
    return null;
  }

  const dados = await resposta.json().catch(() => null);

  if (!resposta.ok) {
    if (resposta.status === 401 && sessao) {
      limparSessao();
      window.location.href = '/login';
    }
    const detalheCampos = dados?.campos
      ? Object.values(dados.campos).join(', ')
      : null;
    throw new Error(detalheCampos || dados?.mensagem || 'Erro na requisição');
  }

  return dados;
}

export const servicoAutenticacao = {
  login: (credenciais) =>
    requisitar('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credenciais),
    }),
  registrar: (cliente) =>
    requisitar('/auth/registrar', {
      method: 'POST',
      body: JSON.stringify(cliente),
    }),
};

export const servicoClientes = {
  listar: () => requisitar('/clientes'),
  buscar: (id) => requisitar(`/clientes/${id}`),
  atualizar: (id, cliente) =>
    requisitar(`/clientes/${id}`, {
      method: 'PUT',
      body: JSON.stringify(cliente),
    }),
  excluir: (id) => requisitar(`/clientes/${id}`, { method: 'DELETE' }),
};

export const servicoAutomoveis = {
  listar: () => requisitar('/automoveis'),
  cadastrar: (automovel) =>
    requisitar('/automoveis', {
      method: 'POST',
      body: JSON.stringify(automovel),
    }),
  atualizar: (id, automovel) =>
    requisitar(`/automoveis/${id}`, {
      method: 'PUT',
      body: JSON.stringify(automovel),
    }),
  excluir: (id) => requisitar(`/automoveis/${id}`, { method: 'DELETE' }),
};

export const servicoAgentes = {
  listarBancos: () => requisitar('/agentes?tipo=BANCO'),
};

export const servicoPedidos = {
  listar: () => requisitar('/pedidos'),
  criar: (pedido) =>
    requisitar('/pedidos', {
      method: 'POST',
      body: JSON.stringify(pedido),
    }),
  modificar: (id, pedido) =>
    requisitar(`/pedidos/${id}`, {
      method: 'PUT',
      body: JSON.stringify(pedido),
    }),
  cancelar: (id) =>
    requisitar(`/pedidos/${id}/cancelar`, {
      method: 'PATCH',
    }),
  avaliar: (id, avaliacao) =>
    requisitar(`/pedidos/${id}/avaliacao`, {
      method: 'PATCH',
      body: JSON.stringify(avaliacao),
    }),
  executarContrato: (id, contrato) =>
    requisitar(`/pedidos/${id}/contrato`, {
      method: 'POST',
      body: JSON.stringify(contrato),
    }),
};

export const servicoContratos = {
  listar: () => requisitar('/contratos'),
};
