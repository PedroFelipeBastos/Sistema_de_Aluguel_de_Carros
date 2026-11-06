package com.aluguelcarros.model;

import com.aluguelcarros.model.enums.Papel;
import com.aluguelcarros.model.enums.TipoAgente;
import jakarta.persistence.*;

@Entity
@Table(name = "agentes")
public class Agente extends Usuario {

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_agente", nullable = false, length = 20)
    private TipoAgente tipoAgente;

    @Override
    public Papel getPapel() {
        return Papel.AGENTE;
    }

    public boolean isBanco() {
        return tipoAgente == TipoAgente.BANCO;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public TipoAgente getTipoAgente() {
        return tipoAgente;
    }

    public void setTipoAgente(TipoAgente tipoAgente) {
        this.tipoAgente = tipoAgente;
    }
}
