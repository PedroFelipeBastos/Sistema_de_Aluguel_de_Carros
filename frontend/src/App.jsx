import { useState } from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Cabecalho from './components/Cabecalho';
import PaginaAutomoveis from './pages/PaginaAutomoveis';
import PaginaCadastro from './pages/PaginaCadastro';
import PaginaClientes from './pages/PaginaClientes';
import PaginaContratos from './pages/PaginaContratos';
import PaginaLogin from './pages/PaginaLogin';
import PaginaPedidos from './pages/PaginaPedidos';
import { obterSessao } from './services/api';
import './App.css';

function RotaProtegida({ usuario, children }) {
  if (!usuario) {
    return <Navigate to="/login" replace />;
  }
  return children;
}

export default function App() {
  const [usuario, setUsuario] = useState(() => obterSessao());

  return (
    <BrowserRouter>
      <Cabecalho usuario={usuario} aoSair={setUsuario} />
      <main className="conteudo-principal">
        <Routes>
          <Route
            path="/login"
            element={
              usuario ? (
                <Navigate to="/pedidos" replace />
              ) : (
                <PaginaLogin aoAutenticar={setUsuario} />
              )
            }
          />
          <Route
            path="/cadastro"
            element={
              usuario ? (
                <Navigate to="/pedidos" replace />
              ) : (
                <PaginaCadastro aoAutenticar={setUsuario} />
              )
            }
          />
          <Route
            path="/pedidos"
            element={
              <RotaProtegida usuario={usuario}>
                <PaginaPedidos usuario={usuario} />
              </RotaProtegida>
            }
          />
          <Route
            path="/automoveis"
            element={
              <RotaProtegida usuario={usuario}>
                <PaginaAutomoveis usuario={usuario} />
              </RotaProtegida>
            }
          />
          <Route
            path="/contratos"
            element={
              <RotaProtegida usuario={usuario}>
                <PaginaContratos />
              </RotaProtegida>
            }
          />
          <Route
            path="/clientes"
            element={
              <RotaProtegida usuario={usuario}>
                <PaginaClientes usuario={usuario} aoSair={setUsuario} />
              </RotaProtegida>
            }
          />
          <Route
            path="*"
            element={<Navigate to={usuario ? '/pedidos' : '/login'} replace />}
          />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
