package com.aluguelcarros.dto;

import com.aluguelcarros.model.Contrato;
import com.aluguelcarros.model.ContratoCredito;
import com.aluguelcarros.model.enums.TipoProprietario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContratoResposta(Long id, Long pedidoId, String clienteNome, AutomovelResposta automovel,
                               String agenteNome, TipoProprietario tipoProprietario, String proprietarioNome,
                               LocalDateTime dataAssinatura, Credito credito) {

    public record Credito(String bancoNome, BigDecimal valor, BigDecimal taxaJurosMensal, Integer numeroParcelas) {

        static Credito de(ContratoCredito credito) {
            if (credito == null) {
                return null;
            }
            return new Credito(credito.getBanco().getNome(), credito.getValor(), credito.getTaxaJurosMensal(),
                    credito.getNumeroParcelas());
        }
    }

    public static ContratoResposta de(Contrato contrato) {
        return new ContratoResposta(contrato.getId(), contrato.getPedido().getId(),
                contrato.getPedido().getCliente().getNome(), AutomovelResposta.de(contrato.getPedido().getAutomovel()),
                contrato.getAgente().getNome(), contrato.getTipoProprietario(), contrato.getProprietario().getNome(),
                contrato.getDataAssinatura(), Credito.de(contrato.getContratoCredito()));
    }
}
