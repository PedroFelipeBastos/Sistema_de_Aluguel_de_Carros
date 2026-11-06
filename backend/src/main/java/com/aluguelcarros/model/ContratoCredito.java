package com.aluguelcarros.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "contratos_credito")
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "banco_id", nullable = false)
    private Agente banco;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "taxa_juros_mensal", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJurosMensal;

    @Column(name = "numero_parcelas", nullable = false)
    private Integer numeroParcelas;

    protected ContratoCredito() {
    }

    public ContratoCredito(Agente banco, BigDecimal valor, BigDecimal taxaJurosMensal, Integer numeroParcelas) {
        this.banco = banco;
        this.valor = valor;
        this.taxaJurosMensal = taxaJurosMensal;
        this.numeroParcelas = numeroParcelas;
    }

    public Long getId() {
        return id;
    }

    public Agente getBanco() {
        return banco;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getTaxaJurosMensal() {
        return taxaJurosMensal;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }
}
