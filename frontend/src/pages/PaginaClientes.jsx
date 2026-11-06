import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { limparSessao, servicoClientes } from '../services/api';

export default function PaginaClientes({ usuario, aoSair }) {
  const ehAgente = usuario?.papel === 'AGENTE';
  const [clientes, setClientes] = useState([]);
  const [clienteSelecionado, setClienteSelecionado] = useState(null);
  const [formulario, setFormulario] = useState({
    nome: '',
    login: '',
    senha: '',
    rg: '',
    cpf: '',
    endereco: '',
    profissao: '',
    rendimentos: [],
  });
  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');
  const navegar = useNavigate();

  useEffect(() => {
    carregarDados();
  }, []);

  async function carregarDados() {
    setErro('');
    try {
      if (ehAgente) {
        const lista = await servicoClientes.listar();
        setClientes(lista);
      } else {
        const dados = await servicoClientes.buscar(usuario.usuarioId);
        selecionarParaEdicao(dados);
      }
    } catch (e) {
      setErro(e.message);
    }
  }

  function selecionarParaEdicao(cliente) {
    setClienteSelecionado(cliente);
    setFormulario({
      nome: cliente.nome,
      login: cliente.login,
      senha: '',
      rg: cliente.rg,
      cpf: cliente.cpf,
      endereco: cliente.endereco,
      profissao: cliente.profissao,
      rendimentos:
        cliente.rendimentos?.length > 0
          ? cliente.rendimentos.map((r) => ({
              entidadeEmpregadora: r.entidadeEmpregadora,
              valor: r.valor,
            }))
          : [{ entidadeEmpregadora: '', valor: '' }],
    });
  }

  function adicionarRendimento() {
    if (formulario.rendimentos.length < 3) {
      setFormulario({
        ...formulario,
        rendimentos: [...formulario.rendimentos, { entidadeEmpregadora: '', valor: '' }],
      });
    }
  }

  function removerRendimento(indice) {
    setFormulario({
      ...formulario,
      rendimentos: formulario.rendimentos.filter((_, i) => i !== indice),
    });
  }

  function atualizarRendimento(indice, campo, valor) {
    const copia = [...formulario.rendimentos];
    copia[indice] = { ...copia[indice], [campo]: valor };
    setFormulario({ ...formulario, rendimentos: copia });
  }

  async function salvarAlteracoes(evento) {
    evento.preventDefault();
    setErro('');
    setMensagem('');
    try {
      const rendimentosValidos = formulario.rendimentos
        .filter((r) => r.entidadeEmpregadora.trim() !== '' && r.valor !== '')
        .map((r) => ({
          entidadeEmpregadora: r.entidadeEmpregadora.trim(),
          valor: Number(r.valor),
        }));

      const payload = {
        ...formulario,
        cpf: formulario.cpf.replace(/\D/g, ''),
        senha: formulario.senha.trim() ? formulario.senha : null,
        rendimentos: rendimentosValidos,
      };

      const atualizado = await servicoClientes.atualizar(clienteSelecionado.id, payload);
      setMensagem('Dados do cliente atualizados com sucesso!');
      if (ehAgente) {
        await carregarDados();
        selecionarParaEdicao(atualizado);
      } else {
        selecionarParaEdicao(atualizado);
      }
    } catch (e) {
      setErro(e.message);
    }
  }

  async function excluirCliente(id) {
    if (!window.confirm('Deseja realmente excluir este cadastro de cliente?')) {
      return;
    }
    setErro('');
    setMensagem('');
    try {
      await servicoClientes.excluir(id);
      if (!ehAgente) {
        limparSessao();
        aoSair(null);
        navegar('/login');
      } else {
        setClienteSelecionado(null);
        await carregarDados();
        setMensagem('Cliente excluído com sucesso.');
      }
    } catch (e) {
      setErro(e.message);
    }
  }

  return (
    <div className="pagina">
      <div className="cabecalho-pagina">
        <h1>{ehAgente ? 'Gerenciamento de Clientes' : 'Meus Dados Cadastrais'}</h1>
      </div>

      {erro && <div className="alerta alerta-erro">{erro}</div>}
      {mensagem && <div className="alerta alerta-sucesso">{mensagem}</div>}

      {ehAgente && (
        <div className="cartao">
          <h3>Clientes Cadastrados</h3>
          {clientes.length === 0 ? (
            <p className="texto-vazio">Nenhum cliente cadastrado.</p>
          ) : (
            <table className="tabela">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>CPF</th>
                  <th>RG</th>
                  <th>Profissão</th>
                  <th>Rendimentos</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {clientes.map((c) => (
                  <tr key={c.id}>
                    <td>#{c.id}</td>
                    <td>{c.nome}</td>
                    <td>{c.cpf}</td>
                    <td>{c.rg}</td>
                    <td>{c.profissao}</td>
                    <td>
                      {c.rendimentos
                        ?.map((r) => `${r.entidadeEmpregadora}: R$ ${Number(r.valor).toFixed(2)}`)
                        .join(' | ') || '-'}
                    </td>
                    <td className="acoes-tabela">
                      <button
                        type="button"
                        className="botao botao-secundario botao-pequeno"
                        onClick={() => selecionarParaEdicao(c)}
                      >
                        Editar
                      </button>
                      <button
                        type="button"
                        className="botao botao-perigo botao-pequeno"
                        onClick={() => excluirCliente(c.id)}
                      >
                        Excluir
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {clienteSelecionado && (
        <div className="cartao">
          <div className="cabecalho-secao">
            <h3>Editar Cliente: {clienteSelecionado.nome}</h3>
            {ehAgente && (
              <button
                type="button"
                className="botao botao-secundario botao-pequeno"
                onClick={() => setClienteSelecionado(null)}
              >
                Fechar
              </button>
            )}
          </div>

          <form onSubmit={salvarAlteracoes} className="formulario">
            <div className="grade-2">
              <div className="campo">
                <label>Nome</label>
                <input
                  value={formulario.nome}
                  onChange={(e) => setFormulario({ ...formulario, nome: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Profissão</label>
                <input
                  value={formulario.profissao}
                  onChange={(e) => setFormulario({ ...formulario, profissao: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>RG</label>
                <input
                  value={formulario.rg}
                  onChange={(e) => setFormulario({ ...formulario, rg: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>CPF</label>
                <input
                  value={formulario.cpf}
                  onChange={(e) => setFormulario({ ...formulario, cpf: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Usuário (Login)</label>
                <input
                  value={formulario.login}
                  onChange={(e) => setFormulario({ ...formulario, login: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Nova Senha (deixe em branco para manter)</label>
                <input
                  type="password"
                  value={formulario.senha}
                  onChange={(e) => setFormulario({ ...formulario, senha: e.target.value })}
                />
              </div>
            </div>

            <div className="campo">
              <label>Endereço</label>
              <input
                value={formulario.endereco}
                onChange={(e) => setFormulario({ ...formulario, endereco: e.target.value })}
                required
              />
            </div>

            <div className="secao-rendimentos">
              <div className="cabecalho-secao">
                <h4>Rendimentos ({formulario.rendimentos.length}/3)</h4>
                {formulario.rendimentos.length < 3 && (
                  <button
                    type="button"
                    className="botao botao-secundario botao-pequeno"
                    onClick={adicionarRendimento}
                  >
                    + Adicionar Rendimento
                  </button>
                )}
              </div>

              {formulario.rendimentos.map((item, indice) => (
                <div key={indice} className="linha-rendimento">
                  <input
                    placeholder="Entidade Empregadora"
                    value={item.entidadeEmpregadora}
                    onChange={(e) =>
                      atualizarRendimento(indice, 'entidadeEmpregadora', e.target.value)
                    }
                    required
                  />
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    placeholder="Valor (R$)"
                    value={item.valor}
                    onChange={(e) => atualizarRendimento(indice, 'valor', e.target.value)}
                    required
                  />
                  <button
                    type="button"
                    className="botao botao-perigo botao-pequeno"
                    onClick={() => removerRendimento(indice)}
                  >
                    Remover
                  </button>
                </div>
              ))}
            </div>

            <div className="acoes-formulario">
              <button type="submit" className="botao botao-primario">
                Salvar Alterações
              </button>
              {!ehAgente && (
                <button
                  type="button"
                  className="botao botao-perigo"
                  onClick={() => excluirCliente(clienteSelecionado.id)}
                >
                  Excluir Minha Conta
                </button>
              )}
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
