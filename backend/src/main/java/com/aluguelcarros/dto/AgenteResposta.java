package com.aluguelcarros.dto;

import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.enums.TipoAgente;

public record AgenteResposta(Long id, String nome, String cnpj, TipoAgente tipoAgente) {

    public static AgenteResposta de(Agente agente) {
        return new AgenteResposta(agente.getId(), agente.getNome(), agente.getCnpj(), agente.getTipoAgente());
    }
}
