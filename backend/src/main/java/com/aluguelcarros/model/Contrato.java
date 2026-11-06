package com.aluguelcarros.model;

import com.aluguelcarros.model.enums.TipoProprietario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proprietario", nullable = false, length = 20)
    private TipoProprietario tipoProprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Usuario proprietario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contrato_credito_id")
    private ContratoCredito contratoCredito;

    @Column(name = "data_assinatura", nullable = false)
    private LocalDateTime dataAssinatura = LocalDateTime.now();

    protected Contrato() {
    }

    public Contrato(Pedido pedido, Agente agente, TipoProprietario tipoProprietario, Usuario proprietario,
                    ContratoCredito contratoCredito) {
        this.pedido = pedido;
        this.agente = agente;
        this.tipoProprietario = tipoProprietario;
        this.proprietario = proprietario;
        this.contratoCredito = contratoCredito;
        pedido.registrarContrato(this);
        pedido.getAutomovel().setProprietario(proprietario);
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Agente getAgente() {
        return agente;
    }

    public TipoProprietario getTipoProprietario() {
        return tipoProprietario;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public ContratoCredito getContratoCredito() {
        return contratoCredito;
    }

    public LocalDateTime getDataAssinatura() {
        return dataAssinatura;
    }
}
