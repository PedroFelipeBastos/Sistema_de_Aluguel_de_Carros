package com.aluguelcarros.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteRequisicao(
        @NotBlank(message = "Informe o nome") @Size(max = 120) String nome,
        @NotBlank(message = "Informe o usuário") @Size(min = 3, max = 60) String login,
        @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") String senha,
        @NotBlank(message = "Informe o RG") @Size(max = 20) String rg,
        @NotBlank(message = "Informe o CPF") @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos") String cpf,
        @NotBlank(message = "Informe o endereço") String endereco,
        @NotBlank(message = "Informe a profissão") @Size(max = 80) String profissao,
        @Size(max = 3, message = "Máximo de 3 rendimentos") List<@Valid RendimentoDTO> rendimentos) {
}
