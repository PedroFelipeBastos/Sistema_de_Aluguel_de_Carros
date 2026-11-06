package com.aluguelcarros.service;

import com.aluguelcarros.dto.AutomovelRequisicao;
import com.aluguelcarros.exception.RecursoNaoEncontradoException;
import com.aluguelcarros.exception.RegraNegocioException;
import com.aluguelcarros.model.Automovel;
import com.aluguelcarros.repository.AutomovelRepository;
import com.aluguelcarros.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutomovelService {

    private final AutomovelRepository automovelRepository;
    private final PedidoRepository pedidoRepository;
    private final AgenteService agenteService;

    public AutomovelService(AutomovelRepository automovelRepository, PedidoRepository pedidoRepository,
                            AgenteService agenteService) {
        this.automovelRepository = automovelRepository;
        this.pedidoRepository = pedidoRepository;
        this.agenteService = agenteService;
    }

    @Transactional(readOnly = true)
    public List<Automovel> listar() {
        return automovelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Automovel buscarPorId(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Automóvel não encontrado"));
    }

    @Transactional
    public Automovel cadastrar(AutomovelRequisicao requisicao, Long agenteId) {
        String placa = normalizarPlaca(requisicao.placa());
        if (automovelRepository.existsByPlaca(placa)) {
            throw new RegraNegocioException("Placa já cadastrada");
        }
        if (automovelRepository.existsByMatricula(requisicao.matricula())) {
            throw new RegraNegocioException("Matrícula já cadastrada");
        }
        Automovel automovel = new Automovel();
        preencherDados(automovel, requisicao, placa);
        automovel.setProprietario(agenteService.buscarPorId(agenteId));
        return automovelRepository.save(automovel);
    }

    @Transactional
    public Automovel atualizar(Long id, AutomovelRequisicao requisicao) {
        Automovel automovel = buscarPorId(id);
        String placa = normalizarPlaca(requisicao.placa());
        if (automovelRepository.existsByPlacaAndIdNot(placa, id)) {
            throw new RegraNegocioException("Placa já cadastrada");
        }
        if (automovelRepository.existsByMatriculaAndIdNot(requisicao.matricula(), id)) {
            throw new RegraNegocioException("Matrícula já cadastrada");
        }
        preencherDados(automovel, requisicao, placa);
        return automovel;
    }

    @Transactional
    public void excluir(Long id) {
        Automovel automovel = buscarPorId(id);
        if (pedidoRepository.existsByAutomovelId(id)) {
            throw new RegraNegocioException("Não é possível excluir um automóvel vinculado a pedidos");
        }
        automovelRepository.delete(automovel);
    }

    private void preencherDados(Automovel automovel, AutomovelRequisicao requisicao, String placa) {
        automovel.setMatricula(requisicao.matricula());
        automovel.setAno(requisicao.ano());
        automovel.setMarca(requisicao.marca());
        automovel.setModelo(requisicao.modelo());
        automovel.setPlaca(placa);
    }

    private String normalizarPlaca(String placa) {
        return placa.replace("-", "").trim().toUpperCase();
    }
}
