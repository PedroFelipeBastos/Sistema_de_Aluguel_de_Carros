package com.aluguelcarros;

import com.aluguelcarros.dto.AvaliacaoRequisicao;
import com.aluguelcarros.dto.ClienteRequisicao;
import com.aluguelcarros.dto.ContratoRequisicao;
import com.aluguelcarros.dto.CreditoRequisicao;
import com.aluguelcarros.dto.LoginRequisicao;
import com.aluguelcarros.dto.PedidoRequisicao;
import com.aluguelcarros.dto.RendimentoDTO;
import com.aluguelcarros.model.enums.StatusPedido;
import com.aluguelcarros.model.enums.TipoAgente;
import com.aluguelcarros.model.enums.TipoProprietario;
import com.aluguelcarros.service.AgenteService;
import com.aluguelcarros.service.AutenticacaoService;
import com.aluguelcarros.service.AutomovelService;
import com.aluguelcarros.service.ContratoService;
import com.aluguelcarros.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SistemaAluguelCarrosApplicationTests {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private AutomovelService automovelService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private AgenteService agenteService;

    @Test
    void fluxoCompletoPedidoAvaliacaoEContrato() {
        var loginEmpresa = autenticacaoService.autenticar(new LoginRequisicao("empresa", "empresa123"));
        assertNotNull(loginEmpresa.token());

        var cadastroCliente = autenticacaoService.registrar(new ClienteRequisicao(
                "João da Silva",
                "joao",
                "senha123",
                "MG1234567",
                "12345678901",
                "Rua das Flores, 100",
                "Engenheiro",
                List.of(new RendimentoDTO("PUC Minas", new BigDecimal("6500.00")))
        ));
        assertNotNull(cadastroCliente.token());

        var automovel = automovelService.listar().getFirst();
        var pedido = pedidoService.criar(
                new PedidoRequisicao(automovel.getId(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)),
                cadastroCliente.usuarioId()
        );
        assertEquals(StatusPedido.PENDENTE, pedido.getStatus());

        var avaliado = pedidoService.avaliar(
                pedido.getId(),
                new AvaliacaoRequisicao(true, "Renda aprovada"),
                loginEmpresa.usuarioId()
        );
        assertEquals(StatusPedido.APROVADO, avaliado.getStatus());

        var banco = agenteService.listar(TipoAgente.BANCO).getFirst();
        var contrato = contratoService.executar(
                pedido.getId(),
                new ContratoRequisicao(
                        TipoProprietario.BANCO,
                        new CreditoRequisicao(banco.getId(), new BigDecimal("10000.00"), new BigDecimal("1.50"), 12)
                ),
                loginEmpresa.usuarioId()
        );
        assertNotNull(contrato.getId());
        assertEquals(StatusPedido.CONTRATADO, pedidoService.buscarPorId(pedido.getId()).getStatus());
    }
}
