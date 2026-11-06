package com.aluguelcarros.repository;

import com.aluguelcarros.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByPedidoClienteIdOrderByDataAssinaturaDesc(Long clienteId);

    List<Contrato> findAllByOrderByDataAssinaturaDesc();
}
