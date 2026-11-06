package com.aluguelcarros.dto;

import com.aluguelcarros.model.Rendimento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RendimentoDTO(
        @NotBlank(message = "Informe a entidade empregadora") String entidadeEmpregadora,
        @NotNull(message = "Informe o valor") @DecimalMin(value = "0.0", message = "Valor não pode ser negativo") BigDecimal valor) {

    public static RendimentoDTO de(Rendimento rendimento) {
        return new RendimentoDTO(rendimento.getEntidadeEmpregadora(), rendimento.getValor());
    }

    public Rendimento paraModelo() {
        return new Rendimento(entidadeEmpregadora, valor);
    }
}
