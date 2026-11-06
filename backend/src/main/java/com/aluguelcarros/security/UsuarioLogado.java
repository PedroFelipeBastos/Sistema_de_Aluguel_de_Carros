package com.aluguelcarros.security;

import com.aluguelcarros.config.ConfiguracaoSeguranca;
import com.aluguelcarros.model.enums.Papel;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public record UsuarioLogado(Long id, Papel papel) {

    public static UsuarioLogado de(Jwt jwt) {
        List<String> papeis = jwt.getClaimAsStringList(ConfiguracaoSeguranca.CLAIM_PAPEL);
        return new UsuarioLogado(Long.valueOf(jwt.getSubject()), Papel.valueOf(papeis.getFirst()));
    }

    public boolean isAgente() {
        return papel == Papel.AGENTE;
    }
}
