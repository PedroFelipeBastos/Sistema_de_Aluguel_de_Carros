package com.aluguelcarros.service;

import com.aluguelcarros.exception.RecursoNaoEncontradoException;
import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.enums.TipoAgente;
import com.aluguelcarros.repository.AgenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgenteService {

    private final AgenteRepository agenteRepository;

    public AgenteService(AgenteRepository agenteRepository) {
        this.agenteRepository = agenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Agente> listar(TipoAgente tipo) {
        return tipo == null ? agenteRepository.findAll() : agenteRepository.findByTipoAgente(tipo);
    }

    public Agente buscarPorId(Long id) {
        return agenteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agente não encontrado"));
    }
}
