import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { salvarSessao, servicoAutenticacao } from '../services/api';

export default function PaginaCadastro({ aoAutenticar }) {
  const [nome, setNome] = useState('');
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [rg, setRg] = useState('');
  const [cpf, setCpf] = useState('');
  const [endereco, setEndereco] = useState('');
  const [profissao, setProfissao] = useState('');
  const [rendimentos, setRendimentos] = useState([
    { entidadeEmpregadora: '', valor: '' },
  ]);
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);
  const navegar = useNavigate();

  function adicionarRendimento() {
    if (rendimentos.length < 3) {
      setRendimentos([...rendimentos, { entidadeEmpregadora: '', valor: '' }]);
    }
  }

  function removerRendimento(indice) {
    setRendimentos(rendimentos.filter((_, i) => i !== indice));
  }

  function atualizarRendimento(indice, campo, valor) {
    const copia = [...rendimentos];
    copia[indice] = { ...copia[indice], [campo]: valor };
    setRendimentos(copia);
  }

  async function enviarFormulario(evento) {
    evento.preventDefault();
    setErro('');
    setCarregando(true);
    try {
      const rendimentosValidos = rendimentos
        .filter((r) => r.entidadeEmpregadora.trim() !== '' && r.valor !== '')
        .map((r) => ({
          entidadeEmpregadora: r.entidadeEmpregadora.trim(),
          valor: Number(r.valor),
        }));

      const dados = await servicoAutenticacao.registrar({
        nome,
        login,
        senha,
        rg,
        cpf: cpf.replace(/\D/g, ''),
        endereco,
        profissao,
        rendimentos: rendimentosValidos,
      });

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
      <div className="cartao cartao-largo">
        <h2>Cadastro de Cliente</h2>
        <p className="subtitulo">Preencha seus dados de identificação e rendimentos (máx. 3)</p>

        {erro && <div className="alerta alerta-erro">{erro}</div>}

        <form onSubmit={enviarFormulario} className="formulario">
          <div className="grade-2">
            <div className="campo">
              <label>Nome Completo</label>
              <input value={nome} onChange={(e) => setNome(e.target.value)} required />
            </div>
            <div className="campo">
              <label>Profissão</label>
              <input value={profissao} onChange={(e) => setProfissao(e.target.value)} required />
            </div>
            <div className="campo">
              <label>RG</label>
              <input value={rg} onChange={(e) => setRg(e.target.value)} required />
            </div>
            <div className="campo">
              <label>CPF (11 dígitos)</label>
              <input
                value={cpf}
                onChange={(e) => setCpf(e.target.value)}
                maxLength={14}
                required
              />
            </div>
            <div className="campo">
              <label>Usuário (Login)</label>
              <input value={login} onChange={(e) => setLogin(e.target.value)} required />
            </div>
            <div className="campo">
              <label>Senha (mín. 6 caracteres)</label>
              <input
                type="password"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                minLength={6}
                required
              />
            </div>
          </div>

          <div className="campo">
            <label>Endereço</label>
            <input value={endereco} onChange={(e) => setEndereco(e.target.value)} required />
          </div>

          <div className="secao-rendimentos">
            <div className="cabecalho-secao">
              <h4>Entidades Empregadoras e Rendimentos ({rendimentos.length}/3)</h4>
              {rendimentos.length < 3 && (
                <button
                  type="button"
                  className="botao botao-secundario botao-pequeno"
                  onClick={adicionarRendimento}
                >
                  + Adicionar Rendimento
                </button>
              )}
            </div>

            {rendimentos.map((item, indice) => (
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
                {rendimentos.length > 1 && (
                  <button
                    type="button"
                    className="botao botao-perigo botao-pequeno"
                    onClick={() => removerRendimento(indice)}
                  >
                    Remover
                  </button>
                )}
              </div>
            ))}
          </div>

          <button type="submit" className="botao botao-primario" disabled={carregando}>
            {carregando ? 'Cadastrando...' : 'Finalizar Cadastro'}
          </button>
        </form>

        <div className="rodape-cartao">
          <span>Já possui conta? </span>
          <Link to="/login">Voltar para o login</Link>
        </div>
      </div>
    </div>
  );
}
