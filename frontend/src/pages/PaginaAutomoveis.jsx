import { useEffect, useState } from 'react';
import { servicoAutomoveis } from '../services/api';

const ESTADO_INICIAL = {
  matricula: '',
  ano: new Date().getFullYear(),
  marca: '',
  modelo: '',
  placa: '',
};

export default function PaginaAutomoveis({ usuario }) {
  const ehAgente = usuario?.papel === 'AGENTE';
  const [automoveis, setAutomoveis] = useState([]);
  const [formulario, setFormulario] = useState(ESTADO_INICIAL);
  const [editandoId, setEditandoId] = useState(null);
  const [erro, setErro] = useState('');
  const [mensagem, setMensagem] = useState('');

  useEffect(() => {
    carregarAutomoveis();
  }, []);

  async function carregarAutomoveis() {
    try {
      const lista = await servicoAutomoveis.listar();
      setAutomoveis(lista);
    } catch (e) {
      setErro(e.message);
    }
  }

  function iniciarEdicao(auto) {
    setEditandoId(auto.id);
    setFormulario({
      matricula: auto.matricula,
      ano: auto.ano,
      marca: auto.marca,
      modelo: auto.modelo,
      placa: auto.placa,
    });
  }

  function cancelarEdicao() {
    setEditandoId(null);
    setFormulario(ESTADO_INICIAL);
  }

  async function salvarAutomovel(evento) {
    evento.preventDefault();
    setErro('');
    setMensagem('');
    try {
      const payload = { ...formulario, ano: Number(formulario.ano) };
      if (editandoId) {
        await servicoAutomoveis.atualizar(editandoId, payload);
        setMensagem('Automóvel atualizado com sucesso!');
      } else {
        await servicoAutomoveis.cadastrar(payload);
        setMensagem('Automóvel cadastrado com sucesso!');
      }
      cancelarEdicao();
      await carregarAutomoveis();
    } catch (e) {
      setErro(e.message);
    }
  }

  async function excluirAutomovel(id) {
    if (!window.confirm('Deseja excluir este automóvel?')) {
      return;
    }
    setErro('');
    setMensagem('');
    try {
      await servicoAutomoveis.excluir(id);
      setMensagem('Automóvel removido.');
      await carregarAutomoveis();
    } catch (e) {
      setErro(e.message);
    }
  }

  return (
    <div className="pagina">
      <div className="cabecalho-pagina">
        <h1>Frota de Automóveis</h1>
      </div>

      {erro && <div className="alerta alerta-erro">{erro}</div>}
      {mensagem && <div className="alerta alerta-sucesso">{mensagem}</div>}

      {ehAgente && (
        <div className="cartao">
          <h3>{editandoId ? 'Editar Automóvel' : 'Cadastrar Novo Automóvel'}</h3>
          <form onSubmit={salvarAutomovel} className="formulario">
            <div className="grade-3">
              <div className="campo">
                <label>Matrícula</label>
                <input
                  value={formulario.matricula}
                  onChange={(e) => setFormulario({ ...formulario, matricula: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Marca</label>
                <input
                  value={formulario.marca}
                  onChange={(e) => setFormulario({ ...formulario, marca: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Modelo</label>
                <input
                  value={formulario.modelo}
                  onChange={(e) => setFormulario({ ...formulario, modelo: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Ano</label>
                <input
                  type="number"
                  min="1950"
                  max="2100"
                  value={formulario.ano}
                  onChange={(e) => setFormulario({ ...formulario, ano: e.target.value })}
                  required
                />
              </div>
              <div className="campo">
                <label>Placa</label>
                <input
                  value={formulario.placa}
                  onChange={(e) => setFormulario({ ...formulario, placa: e.target.value })}
                  maxLength={10}
                  required
                />
              </div>
            </div>

            <div className="acoes-formulario">
              <button type="submit" className="botao botao-primario">
                {editandoId ? 'Atualizar' : 'Cadastrar'}
              </button>
              {editandoId && (
                <button
                  type="button"
                  className="botao botao-secundario"
                  onClick={cancelarEdicao}
                >
                  Cancelar
                </button>
              )}
            </div>
          </form>
        </div>
      )}

      <div className="cartao">
        <h3>Automóveis Disponíveis no Sistema</h3>
        {automoveis.length === 0 ? (
          <p className="texto-vazio">Nenhum automóvel cadastrado.</p>
        ) : (
          <table className="tabela">
            <thead>
              <tr>
                <th>Matrícula</th>
                <th>Marca / Modelo</th>
                <th>Ano</th>
                <th>Placa</th>
                <th>Proprietário Registrado</th>
                {ehAgente && <th>Ações</th>}
              </tr>
            </thead>
            <tbody>
              {automoveis.map((auto) => (
                <tr key={auto.id}>
                  <td>{auto.matricula}</td>
                  <td>
                    {auto.marca} {auto.modelo}
                  </td>
                  <td>{auto.ano}</td>
                  <td>{auto.placa}</td>
                  <td>{auto.proprietarioNome || '-'}</td>
                  {ehAgente && (
                    <td className="acoes-tabela">
                      <button
                        type="button"
                        className="botao botao-secundario botao-pequeno"
                        onClick={() => iniciarEdicao(auto)}
                      >
                        Editar
                      </button>
                      <button
                        type="button"
                        className="botao botao-perigo botao-pequeno"
                        onClick={() => excluirAutomovel(auto.id)}
                      >
                        Excluir
                      </button>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
