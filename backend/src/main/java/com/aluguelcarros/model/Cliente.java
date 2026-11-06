package com.aluguelcarros.model;

import com.aluguelcarros.exception.RegraNegocioException;
import com.aluguelcarros.model.enums.Papel;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

    public static final int MAXIMO_RENDIMENTOS = 3;

    @Column(nullable = false, unique = true, length = 20)
    private String rg;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false, length = 80)
    private String profissao;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rendimentos", joinColumns = @JoinColumn(name = "cliente_id"))
    private List<Rendimento> rendimentos = new ArrayList<>();

    @Override
    public Papel getPapel() {
        return Papel.CLIENTE;
    }

    public void definirRendimentos(List<Rendimento> novosRendimentos) {
        if (novosRendimentos.size() > MAXIMO_RENDIMENTOS) {
            throw new RegraNegocioException("Um cliente pode ter no máximo " + MAXIMO_RENDIMENTOS + " rendimentos");
        }
        rendimentos.clear();
        rendimentos.addAll(novosRendimentos);
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public List<Rendimento> getRendimentos() {
        return rendimentos;
    }
}
