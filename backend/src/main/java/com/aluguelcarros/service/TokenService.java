package com.aluguelcarros.service;

import com.aluguelcarros.config.ConfiguracaoSeguranca;
import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder codificadorJwt;
    private final long expiracaoHoras;

    public TokenService(JwtEncoder codificadorJwt, @Value("${app.jwt.expiracao-horas}") long expiracaoHoras) {
        this.codificadorJwt = codificadorJwt;
        this.expiracaoHoras = expiracaoHoras;
    }

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer("sistema-aluguel-carros")
                .subject(usuario.getId().toString())
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracaoHoras, ChronoUnit.HOURS))
                .claim("nome", usuario.getNome())
                .claim(ConfiguracaoSeguranca.CLAIM_PAPEL, List.of(usuario.getPapel().name()));
        if (usuario instanceof Agente agente) {
            claims.claim("tipoAgente", agente.getTipoAgente().name());
        }
        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        return codificadorJwt.encode(JwtEncoderParameters.from(cabecalho, claims.build())).getTokenValue();
    }
}
