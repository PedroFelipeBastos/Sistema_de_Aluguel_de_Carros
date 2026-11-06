package com.aluguelcarros.controller;

import com.aluguelcarros.dto.AvaliacaoRequisicao;
import com.aluguelcarros.dto.ContratoRequisicao;
import com.aluguelcarros.dto.ContratoResposta;
import com.aluguelcarros.dto.PedidoRequisicao;
import com.aluguelcarros.dto.PedidoResposta;
import com.aluguelcarros.security.UsuarioLogado;
import com.aluguelcarros.service.ContratoService;
import com.aluguelcarros.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ContratoService contratoService;

    public PedidoController(PedidoService pedidoService, ContratoService contratoService) {
        this.pedidoService = pedidoService;
        this.contratoService = contratoService;
    }

    @GetMapping
    public List<PedidoResposta> listar(@AuthenticationPrincipal Jwt jwt) {
        return pedidoService.listar(UsuarioLogado.de(jwt)).stream().map(PedidoResposta::de).toList();
    }

    @GetMapping("/{id}")
    public PedidoResposta buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResposta.de(pedidoService.buscar(id, UsuarioLogado.de(jwt)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResposta criar(@RequestBody @Valid PedidoRequisicao requisicao, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResposta.de(pedidoService.criar(requisicao, UsuarioLogado.de(jwt).id()));
    }

    @PutMapping("/{id}")
    public PedidoResposta modificar(@PathVariable Long id, @RequestBody @Valid PedidoRequisicao requisicao,
                                    @AuthenticationPrincipal Jwt jwt) {
        return PedidoResposta.de(pedidoService.modificar(id, requisicao, UsuarioLogado.de(jwt)));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResposta cancelar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResposta.de(pedidoService.cancelar(id, UsuarioLogado.de(jwt)));
    }

    @PatchMapping("/{id}/avaliacao")
    @PreAuthorize("hasRole('AGENTE')")
    public PedidoResposta avaliar(@PathVariable Long id, @RequestBody @Valid AvaliacaoRequisicao requisicao,
                                  @AuthenticationPrincipal Jwt jwt) {
        return PedidoResposta.de(pedidoService.avaliar(id, requisicao, UsuarioLogado.de(jwt).id()));
    }

    @PostMapping("/{id}/contrato")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('AGENTE')")
    public ContratoResposta executarContrato(@PathVariable Long id, @RequestBody @Valid ContratoRequisicao requisicao,
                                             @AuthenticationPrincipal Jwt jwt) {
        return ContratoResposta.de(contratoService.executar(id, requisicao, UsuarioLogado.de(jwt).id()));
    }
}
