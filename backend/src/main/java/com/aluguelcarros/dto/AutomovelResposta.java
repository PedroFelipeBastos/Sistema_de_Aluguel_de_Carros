package com.aluguelcarros.dto;

import com.aluguelcarros.model.Automovel;

public record AutomovelResposta(Long id, String matricula, Integer ano, String marca, String modelo, String placa,
                                String proprietarioNome) {

    public static AutomovelResposta de(Automovel automovel) {
        String proprietario = automovel.getProprietario() != null ? automovel.getProprietario().getNome() : null;
        return new AutomovelResposta(automovel.getId(), automovel.getMatricula(), automovel.getAno(),
                automovel.getMarca(), automovel.getModelo(), automovel.getPlaca(), proprietario);
    }
}
