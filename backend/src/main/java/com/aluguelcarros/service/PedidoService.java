package com.aluguelcarros.service;

import com.aluguelcarros.dto.AvaliacaoRequisicao;
import com.aluguelcarros.dto.PedidoRequisicao;
import com.aluguelcarros.exception.AcessoNegadoException;
import com.aluguelcarros.exception.RecursoNaoEncontradoException;
import com.aluguelcarros.exception.RegraNegocioException;
import com.aluguelcarros.model.Automovel;
import com.aluguelcarros.model.Pedido;
import com.aluguelcarros.model.enums.StatusPedido;
import com.aluguelcarros.repository.PedidoRepository;
import com.aluguelcarros.security.UsuarioLogado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private static final List<StatusPedido> STATUS_QUE_RESERVAM = List.of(StatusPedido.APROVADO, StatusPedido.CONTRATADO);

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final AutomovelService automovelService;
    private final AgenteService agenteService;

    public PedidoService(PedidoRepository pedidoRepository, ClienteService clienteService,
                         AutomovelService automovelService, AgenteService agenteService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.automovelService = automovelService;
        this.agenteService = agenteService;
    }

    @Transactional
    public Pedido criar(PedidoRequisicao requisicao, Long clienteId) {
        Automovel automovel = automovelService.buscarPorId(requisicao.automovelId());
        verificarDisponibilidade(automovel.getId(), requisicao, 0L);
        Pedido pedido = new Pedido(clienteService.buscarPorId(clienteId), automovel,
                requisicao.dataInicio(), requisicao.dataFim());
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar(UsuarioLogado usuario) {
        return usuario.isAgente()
                ? pedidoRepository.findAllByOrderByDataCriacaoDesc()
                : pedidoRepository.findByClienteIdOrderByDataCriacaoDesc(usuario.id());
    }

    @Transactional(readOnly = true)
    public Pedido buscar(Long id, UsuarioLogado usuario) {
        return buscarComPermissao(id, usuario);
    }

    @Transactional
    public Pedido modificar(Long id, PedidoRequisicao requisicao, UsuarioLogado usuario) {
        Pedido pedido = buscarComPermissao(id, usuario);
        Automovel automovel = automovelService.buscarPorId(requisicao.automovelId());
        verificarDisponibilidade(automovel.getId(), requisicao, pedido.getId());
        pedido.modificar(automovel, requisicao.dataInicio(), requisicao.dataFim());
        return pedido;
    }

    @Transactional
    public Pedido cancelar(Long id, UsuarioLogado usuario) {
        Pedido pedido = buscarComPermissao(id, usuario);
        if (usuario.isAgente()) {
            throw new AcessoNegadoException("Apenas o cliente pode cancelar o pedido");
        }
        pedido.cancelar();
        return pedido;
    }

    @Transactional
    public Pedido avaliar(Long id, AvaliacaoRequisicao requisicao, Long agenteId) {
        Pedido pedido = buscarPorId(id);
        if (requisicao.aprovado()) {
            verificarDisponibilidade(pedido.getAutomovel().getId(),
                    new PedidoRequisicao(pedido.getAutomovel().getId(), pedido.getDataInicio(), pedido.getDataFim()),
                    pedido.getId());
        }
        pedido.avaliar(agenteService.buscarPorId(agenteId), requisicao.aprovado(), requisicao.parecer());
        return pedido;
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    private Pedido buscarComPermissao(Long id, UsuarioLogado usuario) {
        Pedido pedido = buscarPorId(id);
        if (!usuario.isAgente() && !pedido.pertenceAo(usuario.id())) {
            throw new AcessoNegadoException("Acesso negado");
        }
        return pedido;
    }

    private void verificarDisponibilidade(Long automovelId, PedidoRequisicao requisicao, Long ignorarPedidoId) {
        if (pedidoRepository.existeConflitoDeDatas(automovelId, requisicao.dataInicio(), requisicao.dataFim(),
                STATUS_QUE_RESERVAM, ignorarPedidoId)) {
            throw new RegraNegocioException("O automóvel já está reservado neste período");
        }
    }
}
