import { Link, useNavigate } from 'react-router-dom';
import { limparSessao } from '../services/api';

export default function Cabecalho({ usuario, aoSair }) {
  const navegar = useNavigate();

  function sair() {
    limparSessao();
    aoSair(null);
    navegar('/login');
  }

  const ehAgente = usuario?.papel === 'AGENTE';

  return (
    <header className="cabecalho">
      <div className="cabecalho-conteudo">
        <Link to="/" className="marca">
          Sistema de Aluguel de Carros
        </Link>

        {usuario && (
          <nav className="menu">
            <Link to="/pedidos">Pedidos</Link>
            <Link to="/automoveis">Automóveis</Link>
            <Link to="/contratos">Contratos</Link>
            <Link to="/clientes">{ehAgente ? 'Clientes' : 'Meu Perfil'}</Link>
            <span className="usuario-badge">
              {usuario.nome} ({ehAgente ? usuario.tipoAgente : 'CLIENTE'})
            </span>
            <button type="button" className="botao botao-secundario" onClick={sair}>
              Sair
            </button>
          </nav>
        )}
      </div>
    </header>
  );
}
