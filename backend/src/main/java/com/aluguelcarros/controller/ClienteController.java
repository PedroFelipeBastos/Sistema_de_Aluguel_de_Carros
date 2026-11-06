package com.aluguelcarros.controller;

import com.aluguelcarros.dto.ClienteRequisicao;
import com.aluguelcarros.dto.ClienteResposta;
import com.aluguelcarros.security.UsuarioLogado;
import com.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('AGENTE')")
    public List<ClienteResposta> listar() {
        return clienteService.listar().stream().map(ClienteResposta::de).toList();
    }

    @GetMapping("/{id}")
    public ClienteResposta buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ClienteResposta.de(clienteService.buscar(id, UsuarioLogado.de(jwt)));
    }

    @PutMapping("/{id}")
    public ClienteResposta atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequisicao requisicao,
                                     @AuthenticationPrincipal Jwt jwt) {
        return ClienteResposta.de(clienteService.atualizar(id, requisicao, UsuarioLogado.de(jwt)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        clienteService.excluir(id, UsuarioLogado.de(jwt));
    }
}
