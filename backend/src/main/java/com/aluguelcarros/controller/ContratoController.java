package com.aluguelcarros.controller;

import com.aluguelcarros.dto.ContratoResposta;
import com.aluguelcarros.security.UsuarioLogado;
import com.aluguelcarros.service.ContratoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public List<ContratoResposta> listar(@AuthenticationPrincipal Jwt jwt) {
        return contratoService.listar(UsuarioLogado.de(jwt)).stream().map(ContratoResposta::de).toList();
    }

    @GetMapping("/{id}")
    public ContratoResposta buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ContratoResposta.de(contratoService.buscar(id, UsuarioLogado.de(jwt)));
    }
}
