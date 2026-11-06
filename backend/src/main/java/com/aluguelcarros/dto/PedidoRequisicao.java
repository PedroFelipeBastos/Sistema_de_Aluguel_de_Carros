package com.aluguelcarros.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PedidoRequisicao(
        @NotNull(message = "Selecione um automóvel") Long automovelId,
        @NotNull(message = "Informe a data de início") @FutureOrPresent(message = "A data de início não pode estar no passado") LocalDate dataInicio,
        @NotNull(message = "Informe a data de término") LocalDate dataFim) {
}
