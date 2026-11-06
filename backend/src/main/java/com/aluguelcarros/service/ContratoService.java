package com.aluguelcarros.service;

import com.aluguelcarros.dto.ContratoRequisicao;
import com.aluguelcarros.dto.CreditoRequisicao;
import com.aluguelcarros.exception.AcessoNegadoException;
import com.aluguelcarros.exception.RecursoNaoEncontradoException;
import com.aluguelcarros.exception.RegraNegocioException;
import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Contrato;
import com.aluguelcarros.model.ContratoCredito;
import com.aluguelcarros.model.Pedido;
import com.aluguelcarros.model.Usuario;
import com.aluguelcarros.model.enums.TipoAgente;
import com.aluguelcarros.repository.ContratoRepository;
import com.aluguelcarros.security.UsuarioLogado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final PedidoService pedidoService;
    private final AgenteService agenteService;

    public ContratoService(ContratoRepository contratoRepository, PedidoService pedidoService,
                           AgenteService agenteService) {
        this.contratoRepository = contratoRepository;
        this.pedidoService = pedidoService;
        this.agenteService = agenteService;
    }

    @Transactional
    public Contrato executar(Long pedidoId, ContratoRequisicao requisicao, Long agenteId) {
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        Agente agente = agenteService.buscarPorId(agenteId);
        ContratoCredito credito = criarCredito(requisicao.credito());
        Usuario proprietario = definirProprietario(requisicao, pedido, agente, credito);
        Contrato contrato = new Contrato(pedido, agente, requisicao.tipoProprietario(), proprietario, credito);
        return contratoRepository.save(contrato);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listar(UsuarioLogado usuario) {
        return usuario.isAgente()
                ? contratoRepository.findAllByOrderByDataAssinaturaDesc()
                : contratoRepository.findByPedidoClienteIdOrderByDataAssinaturaDesc(usuario.id());
    }

    @Transactional(readOnly = true)
    public Contrato buscar(Long id, UsuarioLogado usuario) {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado"));
        if (!usuario.isAgente() && !contrato.getPedido().pertenceAo(usuario.id())) {
            throw new AcessoNegadoException("Acesso negado");
        }
        return contrato;
    }

    private ContratoCredito criarCredito(CreditoRequisicao requisicao) {
        if (requisicao == null) {
            return null;
        }
        Agente banco = agenteService.buscarPorId(requisicao.bancoId());
        if (!banco.isBanco()) {
            throw new RegraNegocioException("O crédito deve ser concedido por um banco");
        }
        return new ContratoCredito(banco, requisicao.valor(), requisicao.taxaJurosMensal(), requisicao.numeroParcelas());
    }

    private Usuario definirProprietario(ContratoRequisicao requisicao, Pedido pedido, Agente agente,
                                        ContratoCredito credito) {
        return switch (requisicao.tipoProprietario()) {
            case CLIENTE -> pedido.getCliente();
            case EMPRESA -> {
                if (agente.getTipoAgente() != TipoAgente.EMPRESA) {
                    throw new RegraNegocioException("Apenas uma empresa pode ficar como proprietária do automóvel");
                }
                yield agente;
            }
            case BANCO -> {
                if (credito == null) {
                    throw new RegraNegocioException("Para o banco ser proprietário é necessário um contrato de crédito");
                }
                yield credito.getBanco();
            }
        };
    }
}
