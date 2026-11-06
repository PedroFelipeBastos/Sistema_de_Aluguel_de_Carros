package com.aluguelcarros.dto;

import com.aluguelcarros.model.Pedido;
import com.aluguelcarros.model.enums.StatusPedido;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoResposta(Long id, Long clienteId, String clienteNome, AutomovelResposta automovel,
                             LocalDate dataInicio, LocalDate dataFim, StatusPedido status, LocalDateTime dataCriacao,
                             String parecer, String agenteAvaliadorNome, Long contratoId) {

    public static PedidoResposta de(Pedido pedido) {
        String avaliador = pedido.getAgenteAvaliador() != null ? pedido.getAgenteAvaliador().getNome() : null;
        Long contratoId = pedido.getContrato() != null ? pedido.getContrato().getId() : null;
        return new PedidoResposta(pedido.getId(), pedido.getCliente().getId(), pedido.getCliente().getNome(),
                AutomovelResposta.de(pedido.getAutomovel()), pedido.getDataInicio(), pedido.getDataFim(),
                pedido.getStatus(), pedido.getDataCriacao(), pedido.getParecer(), avaliador, contratoId);
    }
}
