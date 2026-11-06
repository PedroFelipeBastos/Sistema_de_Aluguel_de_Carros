package com.aluguelcarros.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreditoRequisicao(
        @NotNull(message = "Selecione o banco") Long bancoId,
        @NotNull(message = "Informe o valor") @DecimalMin(value = "0.01", message = "Valor deve ser positivo") BigDecimal valor,
        @NotNull(message = "Informe a taxa de juros") @DecimalMin(value = "0.0", message = "Taxa inválida") BigDecimal taxaJurosMensal,
        @NotNull(message = "Informe o número de parcelas") @Min(value = 1, message = "Mínimo de 1 parcela") Integer numeroParcelas) {
}
