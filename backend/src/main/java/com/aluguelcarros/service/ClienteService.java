package com.aluguelcarros.service;

import com.aluguelcarros.dto.ClienteRequisicao;
import com.aluguelcarros.dto.RendimentoDTO;
import com.aluguelcarros.exception.AcessoNegadoException;
import com.aluguelcarros.exception.RecursoNaoEncontradoException;
import com.aluguelcarros.exception.RegraNegocioException;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.repository.ClienteRepository;
import com.aluguelcarros.repository.PedidoRepository;
import com.aluguelcarros.repository.UsuarioRepository;
import com.aluguelcarros.security.UsuarioLogado;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder codificadorSenha;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository, PasswordEncoder codificadorSenha) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.codificadorSenha = codificadorSenha;
    }

    @Transactional
    public Cliente cadastrar(ClienteRequisicao requisicao) {
        if (requisicao.senha() == null || requisicao.senha().isBlank()) {
            throw new RegraNegocioException("Informe a senha");
        }
        if (usuarioRepository.existsByLogin(requisicao.login())) {
            throw new RegraNegocioException("Usuário já cadastrado");
        }
        if (clienteRepository.existsByCpf(requisicao.cpf())) {
            throw new RegraNegocioException("CPF já cadastrado");
        }
        if (clienteRepository.existsByRg(requisicao.rg())) {
            throw new RegraNegocioException("RG já cadastrado");
        }
        Cliente cliente = new Cliente();
        cliente.setLogin(requisicao.login());
        preencherDados(cliente, requisicao);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscar(Long id, UsuarioLogado usuario) {
        verificarPermissao(id, usuario);
        return buscarPorId(id);
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequisicao requisicao, UsuarioLogado usuario) {
        verificarPermissao(id, usuario);
        Cliente cliente = buscarPorId(id);
        if (!cliente.getLogin().equals(requisicao.login()) && usuarioRepository.existsByLogin(requisicao.login())) {
            throw new RegraNegocioException("Usuário já cadastrado");
        }
        if (clienteRepository.existsByCpfAndIdNot(requisicao.cpf(), id)) {
            throw new RegraNegocioException("CPF já cadastrado");
        }
        if (clienteRepository.existsByRgAndIdNot(requisicao.rg(), id)) {
            throw new RegraNegocioException("RG já cadastrado");
        }
        cliente.setLogin(requisicao.login());
        preencherDados(cliente, requisicao);
        return cliente;
    }

    @Transactional
    public void excluir(Long id, UsuarioLogado usuario) {
        verificarPermissao(id, usuario);
        Cliente cliente = buscarPorId(id);
        if (pedidoRepository.existsByClienteId(id)) {
            throw new RegraNegocioException("Não é possível excluir um cliente que possui pedidos");
        }
        clienteRepository.delete(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    private void preencherDados(Cliente cliente, ClienteRequisicao requisicao) {
        cliente.setNome(requisicao.nome());
        cliente.setRg(requisicao.rg());
        cliente.setCpf(requisicao.cpf());
        cliente.setEndereco(requisicao.endereco());
        cliente.setProfissao(requisicao.profissao());
        if (requisicao.senha() != null && !requisicao.senha().isBlank()) {
            cliente.setSenha(codificadorSenha.encode(requisicao.senha()));
        }
        List<RendimentoDTO> rendimentos = requisicao.rendimentos() != null ? requisicao.rendimentos() : List.of();
        cliente.definirRendimentos(rendimentos.stream().map(RendimentoDTO::paraModelo).toList());
    }

    private void verificarPermissao(Long clienteId, UsuarioLogado usuario) {
        if (!usuario.isAgente() && !usuario.id().equals(clienteId)) {
            throw new AcessoNegadoException("Acesso negado");
        }
    }
}
