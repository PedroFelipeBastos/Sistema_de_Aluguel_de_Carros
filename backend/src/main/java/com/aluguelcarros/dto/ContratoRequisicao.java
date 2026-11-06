package com.aluguelcarros.dto;

import com.aluguelcarros.model.enums.TipoProprietario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ContratoRequisicao(
        @NotNull(message = "Informe o tipo de proprietário") TipoProprietario tipoProprietario,
        @Valid CreditoRequisicao credito) {
}
