import { useEffect, useState } from 'react';
import { servicoContratos } from '../services/api';

export default function PaginaContratos() {
  const [contratos, setContratos] = useState([]);
  const [erro, setErro] = useState('');

  useEffect(() => {
    servicoContratos
      .listar()
      .then(setContratos)
      .catch((e) => setErro(e.message));
  }, []);

  return (
    <div className="pagina">
      <div className="cabecalho-pagina">
        <h1>Contratos de Aluguel Executados</h1>
      </div>

      {erro && <div className="alerta alerta-erro">{erro}</div>}

      <div className="cartao">
        {contratos.length === 0 ? (
          <p className="texto-vazio">Nenhum contrato formalizado até o momento.</p>
        ) : (
          <table className="tabela">
            <thead>
              <tr>
                <th>Contrato</th>
                <th>Pedido</th>
                <th>Cliente</th>
                <th>Automóvel</th>
                <th>Agente Executor</th>
                <th>Propriedade Registrada</th>
                <th>Contrato de Crédito</th>
              </tr>
            </thead>
            <tbody>
              {contratos.map((c) => (
                <tr key={c.id}>
                  <td>#{c.id}</td>
                  <td>#{c.pedidoId}</td>
                  <td>{c.clienteNome}</td>
                  <td>
                    {c.automovel.marca} {c.automovel.modelo} ({c.automovel.placa})
                  </td>
                  <td>{c.agenteNome}</td>
                  <td>
                    {c.tipoProprietario}: <strong>{c.proprietarioNome}</strong>
                  </td>
                  <td>
                    {c.credito ? (
                      <div>
                        <strong>{c.credito.bancoNome}</strong> — R${' '}
                        {Number(c.credito.valor).toFixed(2)} em {c.credito.numeroParcelas}x (
                        {c.credito.taxaJurosMensal}% a.m.)
                      </div>
                    ) : (
                      'Sem financiamento'
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
