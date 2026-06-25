package br.edu.ifpb.pweb2.psp.trivia;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.ifpb.pweb2.psp.trivia.entities.Corrida;
import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.entities.Perfil;
import br.edu.ifpb.pweb2.psp.trivia.entities.Pergunta;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.repositories.CorridaRepository;
import br.edu.ifpb.pweb2.psp.trivia.repositories.ParticipanteRepository;
import br.edu.ifpb.pweb2.psp.trivia.repositories.ResultadoRepository;

@SpringBootApplication
public class TriviaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriviaApplication.class, args);
    }

    @Bean
    public CommandLineRunner inicializarDados(ParticipanteRepository participanteRepo,
                                              CorridaRepository corridaRepo,
                                              ResultadoRepository resultadoRepo,
                                              PasswordEncoder passwordEncoder) {
        return args -> {
            if (participanteRepo.count() > 0) return;

            // Senha padrão (em texto puro: "123456") para todos os usuários de teste.
            String senhaPadrao = passwordEncoder.encode("123456");

            // --- Participantes ---
            criarParticipante(participanteRepo, "Pedro Lucas", "pepe@gmail.com", true, senhaPadrao);
            criarParticipante(participanteRepo, "Suetone", "suetone@gmail.com", true, senhaPadrao);
            criarParticipante(participanteRepo, "Pedro Arthur", "pedroanery@gmail.com", true, senhaPadrao);
            Participante fred = criarParticipante(participanteRepo, "Fred", "fred@gmail.com", false, senhaPadrao);
            Participante ana = criarParticipante(participanteRepo, "Ana Clara", "anaclara@gmail.com", false, senhaPadrao);
            Participante carlos = criarParticipante(participanteRepo, "Carlos", "carlos44@gmail.com", false, senhaPadrao);
            Participante mariana = criarParticipante(participanteRepo, "Mariana", "mariana@gmail.com", false, senhaPadrao);
            Participante nabu = criarParticipante(participanteRepo, "Nabucodonosor", "nabuquinho08@gmail.com", false, senhaPadrao);

            System.out.println(">>> Participantes criados: " + participanteRepo.count());

            // --- Corridas com perguntas ---
            Corrida matematica = criarCorrida(corridaRepo, "Matemática Básica",
                    "Teste seus conhecimentos em operações e conceitos matemáticos fundamentais.", 60);

            adicionarPergunta(matematica, "Quanto é 7 × 8?", 2,
                    List.of("48", "54", "56", "64"));
            adicionarPergunta(matematica, "Qual é a raiz quadrada de 144?", 1,
                    List.of("11", "12", "13", "14"));
            adicionarPergunta(matematica, "Quanto é 15% de 200?", 0,
                    List.of("30", "25", "35", "20"));
            corridaRepo.save(matematica);

            Corrida spring = criarCorrida(corridaRepo, "Spring Boot",
                    "Perguntas sobre o framework Java mais utilizado para desenvolvimento web.", 90);

            adicionarPergunta(spring, "Qual anotação marca a classe principal de uma aplicação Spring Boot?", 2,
                    List.of("@SpringMain", "@ApplicationBoot", "@SpringBootApplication", "@RunSpring"));
            adicionarPergunta(spring, "Qual starter adiciona suporte a JPA com Hibernate?", 1,
                    List.of("spring-boot-starter-jdbc", "spring-boot-starter-data-jpa",
                            "spring-boot-starter-hibernate", "spring-boot-starter-orm"));
            adicionarPergunta(spring, "Qual anotação mapeia uma requisição HTTP GET em um controller?", 3,
                    List.of("@RequestGet", "@HttpGet", "@Get", "@GetMapping"));
            corridaRepo.save(spring);

            Corrida capitais = criarCorrida(corridaRepo, "Capitais do Brasil",
                    "Você conhece as capitais dos estados brasileiros? Descubra agora!", 60);

            adicionarPergunta(capitais, "Qual é a capital do estado da Paraíba?", 0,
                    List.of("João Pessoa", "Campina Grande", "Recife", "Natal"));
            adicionarPergunta(capitais, "Qual é a capital do estado de Minas Gerais?", 2,
                    List.of("Uberlândia", "Juiz de Fora", "Belo Horizonte", "Ouro Preto"));
            adicionarPergunta(capitais, "Qual é a capital do estado do Amazonas?", 1,
                    List.of("Belém", "Manaus", "Macapá", "Porto Velho"));
            corridaRepo.save(capitais);

            System.out.println(">>> Corridas criadas: " + corridaRepo.count());

            // --- Resultados simulados ---
            LocalDateTime base = LocalDateTime.now().minusDays(3);

            // Matemática (max 3 pts)
            criarResultado(resultadoRepo, fred, matematica, 2, base);
            criarResultado(resultadoRepo, ana, matematica, 3, base.plusHours(1));
            criarResultado(resultadoRepo, carlos, matematica, 1, base.plusHours(2));
            criarResultado(resultadoRepo, mariana, matematica, 2, base.plusHours(3));
            criarResultado(resultadoRepo, nabu, matematica, 3, base.plusHours(4));

            // Spring Boot (max 3 pts)
            criarResultado(resultadoRepo, fred, spring, 1, base.plusDays(1));
            criarResultado(resultadoRepo, ana, spring, 2, base.plusDays(1).plusHours(1));
            criarResultado(resultadoRepo, carlos, spring, 3, base.plusDays(1).plusHours(2));
            criarResultado(resultadoRepo, mariana, spring, 1, base.plusDays(1).plusHours(3));
            criarResultado(resultadoRepo, nabu, spring, 2, base.plusDays(1).plusHours(4));

            // Capitais (max 3 pts)
            criarResultado(resultadoRepo, fred, capitais, 3, base.plusDays(2));
            criarResultado(resultadoRepo, ana, capitais, 2, base.plusDays(2).plusHours(1));
            criarResultado(resultadoRepo, carlos, capitais, 2, base.plusDays(2).plusHours(2));
            criarResultado(resultadoRepo, mariana, capitais, 3, base.plusDays(2).plusHours(3));
            criarResultado(resultadoRepo, nabu, capitais, 1, base.plusDays(2).plusHours(4));

            System.out.println(">>> Resultados simulados: " + resultadoRepo.count());
        };
    }

    private Participante criarParticipante(ParticipanteRepository repo, String nome, String email,
                                           boolean adm, String senhaCriptografada) {
        Participante p = new Participante();
        p.setNome(nome);
        p.setEmail(email);
        p.setSenha(senhaCriptografada);
        p.setPerfis(adm ? Set.of(Perfil.ADMIN) : Set.of(Perfil.PARTICIPANTE));
        return repo.save(p);
    }

    private Corrida criarCorrida(CorridaRepository repo, String titulo, String descricao, int tempo) {
        Corrida c = new Corrida();
        c.setTitulo(titulo);
        c.setDescricao(descricao);
        c.setTempo(tempo);
        c.setAtivo(true);
        return repo.save(c);
    }

    private void adicionarPergunta(Corrida corrida, String enunciado, int resposta, List<String> alternativas) {
        Pergunta p = new Pergunta();
        p.setEnunciado(enunciado);
        p.setResposta(resposta);
        p.setIdCorrida(corrida);
        p.setAlternativas(alternativas);
        corrida.getPerguntas().add(p);
    }

    private void criarResultado(ResultadoRepository repo, Participante participante,
                                Corrida corrida, int pontos, LocalDateTime dataHora) {
        Resultado r = new Resultado();
        r.setPontuacao(BigDecimal.valueOf(pontos));
        r.setDataHora(dataHora);
        r.setIdParticipante(participante);
        r.setIdCorrida(corrida);
        repo.save(r);
    }
}
