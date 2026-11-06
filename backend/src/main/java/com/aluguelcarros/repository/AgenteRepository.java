package com.aluguelcarros.repository;

import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.enums.TipoAgente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenteRepository extends JpaRepository<Agente, Long> {

    List<Agente> findByTipoAgente(TipoAgente tipoAgente);
}
