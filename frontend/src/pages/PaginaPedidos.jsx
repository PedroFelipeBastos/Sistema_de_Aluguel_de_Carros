import { useEffect, useState } from 'react';
import {
  servicoAgentes,
  servicoAutomoveis,
  servicoPedidos,
} from '../services/api';

function dataHoje() {
  return new Date().toISOString().split('T')[0];
}

function dataAmanha() {
  const d = new Date();
  d.setDate(d.getDate() + 1);
  return d.toISOString().split('T')[0];
}

export default function PaginaPedidos({ usuario }) {
  const ehAgente = usuario?.papel === 'AGENTE';
  const [pedidos, setPedidos] = useState([]);
  const [automoveis, setAutomoveis] = useState([]);
  const [bancos, setBancos] = useState([]);

  const [automovelId, setAutomovelId] = useState('');
  const [dataInicio, setDataInicio] = useState(dataHoje());
  const [dataFim, setDataFim] = useState(dataAmanha());
  const [pedidoEditando, setPedidoEditando] = useState(null);

  const [pedidoAvaliando, setPedidoAvaliando] = useState(null);
  const [aprovado, setAprovado] = useState(true);
  const [parecer, setParecer] = useState('');

  const [pedidoContratando, setPedidoContratando] = useState(null);
  const [tipoProprietario, setTipoProprietario] = useState('CLIENTE');
  const [associarCredito, setAssociarCredito] = useState(false);
  const [bancoId, setBancoId] = useState('');
  const [valorCredito, setValorCredito] = useState('');
  const [taxaJurosMensal, setTaxaJurosMensal] = useState('1.5');
  const [numeroParcelas, setNumeroParcelas] = useState('12');

  const [erro, setErro] = useState('');
  const [mensagem, setMensagem] = useState('');

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    setErro('');
    try {
      const [listaPedidos, listaAutos] = await Promise.all([
        servicoPedidos.listar(),
        servicoAutomoveis.listar(),
      ]);
      setPedidos(listaPedidos);
      setAutomoveis(listaAutos);
      if (listaAutos.length > 0 && !automovelId) {
        setAutomovelId(String(listaAutos[0].id));
      }
      if (ehAgente) {
        const listaBancos = await servicoAgentes.listarBancos();
        setBancos(listaBancos);
        if (listaBancos.length > 0) {
          setBancoId(String(listaBancos[0].id));
        }
      }
    } catch (e) {
      setErro(e.message);
    }
  }

  function iniciarModificacao(pedido) {
    setPedidoEditando(pedido);
    setAutomovelId(String(pedido.automovel.id));
    setDataInicio(pedido.dataInicio);
    setDataFim(pedido.dataFim);
  }

  function limparFormularioPedido() {
    setPedidoEditando(null);
    if (automoveis.length > 0) {
      setAutomovelId(String(automoveis[0].id));
    }
    setDataInicio(dataHoje());
    setDataFim(dataAmanha());
  }

  async function salvarPedido(evento) {
    evento.preventDefault();
    setErro('');
    setMensagem('');
    try {
      const payload = {
        automovelId: Number(automovelId),
        dataInicio,
        dataFim,
      };
      if (pedidoEditando) {
        await servicoPedidos.modificar(pedidoEditando.id, payload);
        setMensagem(`Pedido #${pedidoEditando.id} modificado com sucesso!`);
      } else {
        await servicoPedidos.criar(payload);
        setMensagem('Pedido de aluguel introduzido com sucesso!');
      }
      limparFormularioPedido();
      await carregarTudo();
    } catch (e) {
      setErro(e.message);
    }
  }

  async function cancelarPedido(id) {
    if (!window.confirm(`Confirmar cancelamento do pedido #${id}?`)) {
      return;
    }
    setErro('');
    setMensagem('');
    try {
      await servicoPedidos.cancelar(id);
      setMensagem(`Pedido #${id} cancelado.`);
      await carregarTudo();
    } catch (e) {
      setErro(e.message);
    }
  }

  async function enviarAvaliacao(evento) {
    evento.preventDefault();
    setErro('');
    setMensagem('');
    try {
      await servicoPedidos.avaliar(pedidoAvaliando.id, { aprovado, parecer });
      setMensagem(`Pedido #${pedidoAvaliando.id} avaliado com sucesso!`);
      setPedidoAvaliando(null);
      setParecer('');
      await carregarTudo();
    } catch (e) {
      setErro(e.message);
    }
  }

  function abrirModalContrato(pedido) {
    setPedidoContratando(pedido);
    setTipoProprietario(usuario?.tipoAgente === 'EMPRESA' ? 'EMPRESA' : 'CLIENTE');
    setAssociarCredito(false);
    setValorCredito('5000');
  }

  async function confirmarContrato(evento) {
    evento.preventDefault();
    setErro('');
    setMensagem('');
    try {
      const usaCredito = associarCredito || tipoProprietario === 'BANCO';
      const payload = {
        tipoProprietario,
        credito: usaCredito
          ? {
              bancoId: Number(bancoId),
              valor: Number(valorCredito),
              taxaJurosMensal: Number(taxaJurosMensal),
              numeroParcelas: Number(numeroParcelas),
            }
          : null,
      };
      await servicoPedidos.executarContrato(pedidoContratando.id, payload);
      setMensagem(`Contrato executado para o pedido #${pedidoContratando.id}!`);
      setPedidoContratando(null);
      await carregarTudo();
    } catch (e) {
      setErro(e.message);
    }
  }

  const mostrarFormularioPedido = !ehAgente || pedidoEditando !== null;

  return (
    <div className="pagina">
      <div className="cabecalho-pagina">
        <h1>{ehAgente ? 'Gestão e Avaliação de Pedidos' : 'Meus Pedidos de Aluguel'}</h1>
      </div>

      {erro && <div className="alerta alerta-erro">{erro}</div>}
      {mensagem && <div className="alerta alerta-sucesso">{mensagem}</div>}

      {mostrarFormularioPedido && (
        <div className="cartao">
          <h3>
            {pedidoEditando
              ? `Modificar Pedido #${pedidoEditando.id}`
              : 'Novo Pedido de Aluguel'}
          </h3>
          <form onSubmit={salvarPedido} className="formulario">
            <div className="grade-3">
              <div className="campo">
                <label>Automóvel</label>
                <select
                  value={automovelId}
                  onChange={(e) => setAutomovelId(e.target.value)}
                  required
                >
                  {automoveis.map((auto) => (
                    <option key={auto.id} value={auto.id}>
                      {auto.marca} {auto.modelo} ({auto.ano}) - Placa {auto.placa}
                    </option>
                  ))}
                </select>
              </div>
              <div className="campo">
                <label>Data de Início</label>
                <input
                  type="date"
                  value={dataInicio}
                  onChange={(e) => setDataInicio(e.target.value)}
                  required
                />
              </div>
              <div className="campo">
                <label>Data de Término</label>
                <input
                  type="date"
                  value={dataFim}
                  onChange={(e) => setDataFim(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="acoes-formulario">
              <button type="submit" className="botao botao-primario">
                {pedidoEditando ? 'Salvar Modificações' : 'Criar Pedido'}
              </button>
              {pedidoEditando && (
                <button
                  type="button"
                  className="botao botao-secundario"
                  onClick={limparFormularioPedido}
                >
                  Cancelar Edição
                </button>
              )}
            </div>
          </form>
        </div>
      )}

      {pedidoAvaliando && (
        <div className="cartao destaque">
          <h3>Avaliação Financeira - Pedido #{pedidoAvaliando.id}</h3>
          <p className="subtitulo">
            Cliente: <strong>{pedidoAvaliando.clienteNome}</strong> | Automóvel:{' '}
            <strong>
              {pedidoAvaliando.automovel.marca} {pedidoAvaliando.automovel.modelo}
            </strong>
          </p>
          <form onSubmit={enviarAvaliacao} className="formulario">
            <div className="grade-2">
              <div className="campo">
                <label>Parecer Financeiro</label>
                <select
                  value={aprovado ? 'true' : 'false'}
                  onChange={(e) => setAprovado(e.target.value === 'true')}
                >
                  <option value="true">Positivo (Aprovar Pedido)</option>
                  <option value="false">Negativo (Reprovar Pedido)</option>
                </select>
              </div>
              <div className="campo">
                <label>Observações / Justificativa</label>
                <input
                  value={parecer}
                  onChange={(e) => setParecer(e.target.value)}
                  placeholder="Ex.: Renda compatível com o contrato"
                  required
                />
              </div>
            </div>
            <div className="acoes-formulario">
              <button type="submit" className="botao botao-primario">
                Registrar Parecer
              </button>
              <button
                type="button"
                className="botao botao-secundario"
                onClick={() => setPedidoAvaliando(null)}
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      {pedidoContratando && (
        <div className="cartao destaque">
          <h3>Executar Contrato - Pedido #{pedidoContratando.id}</h3>
          <form onSubmit={confirmarContrato} className="formulario">
            <div className="grade-2">
              <div className="campo">
                <label>Registro de Propriedade do Automóvel</label>
                <select
                  value={tipoProprietario}
                  onChange={(e) => setTipoProprietario(e.target.value)}
                >
                  <option value="CLIENTE">Cliente ({pedidoContratando.clienteNome})</option>
                  {usuario?.tipoAgente === 'EMPRESA' && (
                    <option value="EMPRESA">Empresa Agente ({usuario.nome})</option>
                  )}
                  <option value="BANCO">Banco Agente (requer contrato de crédito)</option>
                </select>
              </div>

              <div className="campo campo-checkbox">
                <label>
                  <input
                    type="checkbox"
                    checked={associarCredito || tipoProprietario === 'BANCO'}
                    disabled={tipoProprietario === 'BANCO'}
                    onChange={(e) => setAssociarCredito(e.target.checked)}
                  />
                  Associar Contrato de Crédito Bancário
                </label>
              </div>
            </div>

            {(associarCredito || tipoProprietario === 'BANCO') && (
              <div className="grade-2">
                <div className="campo">
                  <label>Banco Concessor</label>
                  <select
                    value={bancoId}
                    onChange={(e) => setBancoId(e.target.value)}
                    required
                  >
                    {bancos.map((b) => (
                      <option key={b.id} value={b.id}>
                        {b.nome} (CNPJ: {b.cnpj})
                      </option>
                    ))}
                  </select>
                </div>
                <div className="campo">
                  <label>Valor Financiado (R$)</label>
                  <input
                    type="number"
                    step="0.01"
                    min="1"
                    value={valorCredito}
                    onChange={(e) => setValorCredito(e.target.value)}
                    required
                  />
                </div>
                <div className="campo">
                  <label>Taxa de Juros Mensal (%)</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    value={taxaJurosMensal}
                    onChange={(e) => setTaxaJurosMensal(e.target.value)}
                    required
                  />
                </div>
                <div className="campo">
                  <label>Número de Parcelas</label>
                  <input
                    type="number"
                    min="1"
                    value={numeroParcelas}
                    onChange={(e) => setNumeroParcelas(e.target.value)}
                    required
                  />
                </div>
              </div>
            )}

            <div className="acoes-formulario">
              <button type="submit" className="botao botao-primario">
                Assinar e Executar Contrato
              </button>
              <button
                type="button"
                className="botao botao-secundario"
                onClick={() => setPedidoContratando(null)}
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="cartao">
        <h3>Lista de Pedidos</h3>
        {pedidos.length === 0 ? (
          <p className="texto-vazio">Nenhum pedido registrado até o momento.</p>
        ) : (
          <table className="tabela">
            <thead>
              <tr>
                <th>ID</th>
                {ehAgente && <th>Cliente</th>}
                <th>Automóvel</th>
                <th>Período</th>
                <th>Status</th>
                <th>Parecer Financeiro</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {pedidos.map((p) => (
                <tr key={p.id}>
                  <td>#{p.id}</td>
                  {ehAgente && <td>{p.clienteNome}</td>}
                  <td>
                    {p.automovel.marca} {p.automovel.modelo} ({p.automovel.placa})
                  </td>
                  <td>
                    {p.dataInicio} até {p.dataFim}
                  </td>
                  <td>
                    <span className={`status status-${p.status.toLowerCase()}`}>
                      {p.status}
                    </span>
                  </td>
                  <td>
                    {p.parecer ? (
                      <span>
                        {p.parecer}{' '}
                        {p.agenteAvaliadorNome && <small>({p.agenteAvaliadorNome})</small>}
                      </span>
                    ) : (
                      '-'
                    )}
                  </td>
                  <td className="acoes-tabela">
                    {p.status === 'PENDENTE' && (
                      <button
                        type="button"
                        className="botao botao-secundario botao-pequeno"
                        onClick={() => iniciarModificacao(p)}
                      >
                        Modificar
                      </button>
                    )}

                    {!ehAgente && (p.status === 'PENDENTE' || p.status === 'APROVADO') && (
                      <button
                        type="button"
                        className="botao botao-perigo botao-pequeno"
                        onClick={() => cancelarPedido(p.id)}
                      >
                        Cancelar
                      </button>
                    )}

                    {ehAgente && p.status === 'PENDENTE' && (
                      <button
                        type="button"
                        className="botao botao-primario botao-pequeno"
                        onClick={() => {
                          setPedidoAvaliando(p);
                          setAprovado(true);
                          setParecer('');
                        }}
                      >
                        Avaliar
                      </button>
                    )}

                    {ehAgente && p.status === 'APROVADO' && (
                      <button
                        type="button"
                        className="botao botao-sucesso botao-pequeno"
                        onClick={() => abrirModalContrato(p)}
                      >
                        Executar Contrato
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
