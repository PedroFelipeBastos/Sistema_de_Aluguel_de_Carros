package com.aluguelcarros.dto;

import com.aluguelcarros.model.enums.Papel;
import com.aluguelcarros.model.enums.TipoAgente;

public record LoginResposta(String token, Long usuarioId, String nome, Papel papel, TipoAgente tipoAgente) {
}
