package com.aluguelcarros.dto;

import com.aluguelcarros.model.Cliente;

import java.util.List;

public record ClienteResposta(Long id, String nome, String login, String rg, String cpf, String endereco,
                              String profissao, List<RendimentoDTO> rendimentos) {

    public static ClienteResposta de(Cliente cliente) {
        return new ClienteResposta(cliente.getId(), cliente.getNome(), cliente.getLogin(), cliente.getRg(),
                cliente.getCpf(), cliente.getEndereco(), cliente.getProfissao(),
                cliente.getRendimentos().stream().map(RendimentoDTO::de).toList());
    }
}
