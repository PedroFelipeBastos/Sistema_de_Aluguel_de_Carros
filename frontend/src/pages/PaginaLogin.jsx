import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { salvarSessao, servicoAutenticacao } from '../services/api';

export default function PaginaLogin({ aoAutenticar }) {
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);
  const navegar = useNavigate();

  async function enviarFormulario(evento) {
    evento.preventDefault();
    setErro('');
    setCarregando(true);
    try {
      const dados = await servicoAutenticacao.login({ login, senha });
      salvarSessao(dados);
      aoAutenticar(dados);
      navegar('/pedidos');
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="container-autenticacao">
      <div className="cartao">
        <h2>Entrar no Sistema</h2>
        <p className="subtitulo">Acesse sua conta de cliente ou agente</p>

        {erro && <div className="alerta alerta-erro">{erro}</div>}

        <form onSubmit={enviarFormulario} className="formulario">
          <div className="campo">
            <label htmlFor="login">Usuário</label>
            <input
              id="login"
              type="text"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              required
            />
          </div>

          <div className="campo">
            <label htmlFor="senha">Senha</label>
            <input
              id="senha"
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="botao botao-primario" disabled={carregando}>
            {carregando ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <div className="rodape-cartao">
          <span>Ainda não possui cadastro? </span>
          <Link to="/cadastro">Criar conta de cliente</Link>
        </div>

        <div className="dica-acesso">
          <strong>Agentes pré-cadastrados para teste:</strong>
          <div>Empresa: <code>empresa</code> / <code>empresa123</code></div>
          <div>Banco: <code>banco</code> / <code>banco123</code></div>
        </div>
      </div>
    </div>
  );
}
