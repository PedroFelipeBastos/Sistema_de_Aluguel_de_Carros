package com.aluguelcarros.config;

import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Automovel;
import com.aluguelcarros.model.enums.TipoAgente;
import com.aluguelcarros.repository.AgenteRepository;
import com.aluguelcarros.repository.AutomovelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CargaInicialDados implements CommandLineRunner {

    private final AgenteRepository agenteRepository;
    private final AutomovelRepository automovelRepository;
    private final PasswordEncoder codificadorSenha;

    public CargaInicialDados(AgenteRepository agenteRepository, AutomovelRepository automovelRepository,
                             PasswordEncoder codificadorSenha) {
        this.agenteRepository = agenteRepository;
        this.automovelRepository = automovelRepository;
        this.codificadorSenha = codificadorSenha;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (agenteRepository.count() > 0) {
            return;
        }
        Agente empresa = agenteRepository.save(
                criarAgente("Locadora Central", "empresa", "empresa123", "12345678000190", TipoAgente.EMPRESA));
        agenteRepository.save(
                criarAgente("Banco Aluguel S.A.", "banco", "banco123", "98765432000110", TipoAgente.BANCO));

        automovelRepository.save(criarAutomovel("MAT-0001", 2022, "Fiat", "Argo", "ABC1D23", empresa));
        automovelRepository.save(criarAutomovel("MAT-0002", 2023, "Volkswagen", "Polo", "EFG4H56", empresa));
        automovelRepository.save(criarAutomovel("MAT-0003", 2021, "Chevrolet", "Onix", "IJK7L89", empresa));
    }

    private Agente criarAgente(String nome, String login, String senha, String cnpj, TipoAgente tipo) {
        Agente agente = new Agente();
        agente.setNome(nome);
        agente.setLogin(login);
        agente.setSenha(codificadorSenha.encode(senha));
        agente.setCnpj(cnpj);
        agente.setTipoAgente(tipo);
        return agente;
    }

    private Automovel criarAutomovel(String matricula, int ano, String marca, String modelo, String placa,
                                     Agente proprietario) {
        Automovel automovel = new Automovel();
        automovel.setMatricula(matricula);
        automovel.setAno(ano);
        automovel.setMarca(marca);
        automovel.setModelo(modelo);
        automovel.setPlaca(placa);
        automovel.setProprietario(proprietario);
        return automovel;
    }
}
