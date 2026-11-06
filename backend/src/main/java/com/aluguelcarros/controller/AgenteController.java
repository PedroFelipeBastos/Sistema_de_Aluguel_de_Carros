package com.aluguelcarros.controller;

import com.aluguelcarros.dto.AgenteResposta;
import com.aluguelcarros.model.enums.TipoAgente;
import com.aluguelcarros.service.AgenteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }

    @GetMapping
    public List<AgenteResposta> listar(@RequestParam(required = false) TipoAgente tipo) {
        return agenteService.listar(tipo).stream().map(AgenteResposta::de).toList();
    }
}
