package com.aluguelcarros.controller;

import com.aluguelcarros.dto.AutomovelRequisicao;
import com.aluguelcarros.dto.AutomovelResposta;
import com.aluguelcarros.security.UsuarioLogado;
import com.aluguelcarros.service.AutomovelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automoveis")
public class AutomovelController {

    private final AutomovelService automovelService;

    public AutomovelController(AutomovelService automovelService) {
        this.automovelService = automovelService;
    }

    @GetMapping
    public List<AutomovelResposta> listar() {
        return automovelService.listar().stream().map(AutomovelResposta::de).toList();
    }

    @GetMapping("/{id}")
    public AutomovelResposta buscar(@PathVariable Long id) {
        return AutomovelResposta.de(automovelService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('AGENTE')")
    public AutomovelResposta cadastrar(@RequestBody @Valid AutomovelRequisicao requisicao,
                                       @AuthenticationPrincipal Jwt jwt) {
        return AutomovelResposta.de(automovelService.cadastrar(requisicao, UsuarioLogado.de(jwt).id()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AGENTE')")
    public AutomovelResposta atualizar(@PathVariable Long id, @RequestBody @Valid AutomovelRequisicao requisicao) {
        return AutomovelResposta.de(automovelService.atualizar(id, requisicao));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('AGENTE')")
    public void excluir(@PathVariable Long id) {
        automovelService.excluir(id);
    }
}
