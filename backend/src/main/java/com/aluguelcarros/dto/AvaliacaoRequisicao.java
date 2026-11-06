package com.aluguelcarros.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequisicao(
        @NotNull(message = "Informe se o pedido foi aprovado") Boolean aprovado,
        @Size(max = 500) String parecer) {
}
