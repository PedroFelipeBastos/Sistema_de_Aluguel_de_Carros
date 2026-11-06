package com.aluguelcarros.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequisicao(
        @NotBlank(message = "Informe o usuário") String login,
        @NotBlank(message = "Informe a senha") String senha) {
}
