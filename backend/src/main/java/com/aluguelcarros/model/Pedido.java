package com.aluguelcarros.model;

import com.aluguelcarros.exception.RegraNegocioException;
import com.aluguelcarros.model.enums.StatusPedido;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "automovel_id", nullable = false)
    private Automovel automovel;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(length = 500)
    private String parecer;

    @ManyToOne
    @JoinColumn(name = "agente_avaliador_id")
    private Agente agenteAvaliador;

    @OneToOne(mappedBy = "pedido")
    private Contrato contrato;

    protected Pedido() {
    }

    public Pedido(Cliente cliente, Automovel automovel, LocalDate dataInicio, LocalDate dataFim) {
        this.cliente = cliente;
        definirDados(automovel, dataInicio, dataFim);
    }

    public void modificar(Automovel automovel, LocalDate dataInicio, LocalDate dataFim) {
        if (status != StatusPedido.PENDENTE) {
            throw new RegraNegocioException("Apenas pedidos pendentes podem ser modificados");
        }
        definirDados(automovel, dataInicio, dataFim);
    }

    public void cancelar() {
        if (status != StatusPedido.PENDENTE && status != StatusPedido.APROVADO) {
            throw new RegraNegocioException("Este pedido não pode mais ser cancelado");
        }
        status = StatusPedido.CANCELADO;
    }

    public void avaliar(Agente agente, boolean aprovado, String parecer) {
        if (status != StatusPedido.PENDENTE) {
            throw new RegraNegocioException("Apenas pedidos pendentes podem ser avaliados");
        }
        this.agenteAvaliador = agente;
        this.parecer = parecer;
        this.status = aprovado ? StatusPedido.APROVADO : StatusPedido.REPROVADO;
    }

    public void registrarContrato(Contrato contrato) {
        if (status != StatusPedido.APROVADO) {
            throw new RegraNegocioException("Apenas pedidos aprovados podem gerar contrato");
        }
        this.contrato = contrato;
        this.status = StatusPedido.CONTRATADO;
    }

    public boolean pertenceAo(Long clienteId) {
        return cliente.getId().equals(clienteId);
    }

    private void definirDados(Automovel automovel, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null || !dataFim.isAfter(dataInicio)) {
            throw new RegraNegocioException("A data de término deve ser posterior à data de início");
        }
        this.automovel = automovel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getParecer() {
        return parecer;
    }

    public Agente getAgenteAvaliador() {
        return agenteAvaliador;
    }

    public Contrato getContrato() {
        return contrato;
    }
}
