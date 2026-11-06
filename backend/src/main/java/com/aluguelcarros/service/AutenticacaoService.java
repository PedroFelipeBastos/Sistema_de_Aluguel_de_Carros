package com.aluguelcarros.service;

import com.aluguelcarros.dto.ClienteRequisicao;
import com.aluguelcarros.dto.LoginRequisicao;
import com.aluguelcarros.dto.LoginResposta;
import com.aluguelcarros.exception.CredenciaisInvalidasException;
import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.model.Usuario;
import com.aluguelcarros.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteService clienteService;
    private final PasswordEncoder codificadorSenha;
    private final TokenService tokenService;

    public AutenticacaoService(UsuarioRepository usuarioRepository, ClienteService clienteService,
                               PasswordEncoder codificadorSenha, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.clienteService = clienteService;
        this.codificadorSenha = codificadorSenha;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public LoginResposta autenticar(LoginRequisicao requisicao) {
        Usuario usuario = usuarioRepository.findByLogin(requisicao.login())
                .filter(u -> codificadorSenha.matches(requisicao.senha(), u.getSenha()))
                .orElseThrow(CredenciaisInvalidasException::new);
        return gerarResposta(usuario);
    }

    @Transactional
    public LoginResposta registrar(ClienteRequisicao requisicao) {
        Cliente cliente = clienteService.cadastrar(requisicao);
        return gerarResposta(cliente);
    }

    private LoginResposta gerarResposta(Usuario usuario) {
        var tipoAgente = usuario instanceof Agente agente ? agente.getTipoAgente() : null;
        return new LoginResposta(tokenService.gerarToken(usuario), usuario.getId(), usuario.getNome(),
                usuario.getPapel(), tipoAgente);
    }
}
