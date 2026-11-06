package com.aluguelcarros.repository;

import com.aluguelcarros.model.Pedido;
import com.aluguelcarros.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);

    List<Pedido> findAllByOrderByDataCriacaoDesc();

    boolean existsByAutomovelId(Long automovelId);

    boolean existsByClienteId(Long clienteId);

    @Query("""
            select count(p) > 0 from Pedido p
            where p.automovel.id = :automovelId
              and p.status in :status
              and p.id <> :ignorarId
              and p.dataInicio < :dataFim
              and p.dataFim > :dataInicio
            """)
    boolean existeConflitoDeDatas(@Param("automovelId") Long automovelId,
                                  @Param("dataInicio") LocalDate dataInicio,
                                  @Param("dataFim") LocalDate dataFim,
                                  @Param("status") Collection<StatusPedido> status,
                                  @Param("ignorarId") Long ignorarId);
}
