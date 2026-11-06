package com.aluguelcarros.controller;

import com.aluguelcarros.dto.ClienteRequisicao;
import com.aluguelcarros.dto.LoginRequisicao;
import com.aluguelcarros.dto.LoginResposta;
import com.aluguelcarros.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public LoginResposta login(@RequestBody @Valid LoginRequisicao requisicao) {
        return autenticacaoService.autenticar(requisicao);
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResposta registrar(@RequestBody @Valid ClienteRequisicao requisicao) {
        return autenticacaoService.registrar(requisicao);
    }
}
