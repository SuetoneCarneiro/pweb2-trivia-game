package br.edu.ifpb.pweb2.psp.trivia;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;
import br.edu.ifpb.pweb2.psp.trivia.repositories.ParticipanteRepository;

@SpringBootApplication
public class TriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriviaApplication.class, args);
	}

    // Teste para verificar se o banco de dados está funcionando corretamente
	@Bean 
    public CommandLineRunner testarBanco(ParticipanteRepository repo) {
        return args -> {
            // fazendo essa checagem aqui a gnt garante que o banco 
            // de dados está vazio. Só cria se não existir participante
            if (repo.count() == 0) {
                System.out.println(">>> A tentar salvar participante de teste...");
                
                Participante p = new Participante();
                p.setNome("Pedro Lucas");
                p.setEmail("pepe@gmail.com");
                p.setAdm(true);
                
                repo.save(p); // O JPA gera o INSERT sozinho

                Participante comum = new Participante();
                comum.setNome("Usuario");
                comum.setEmail("usuario@trivia.com");
                comum.setAdm(false);

                repo.save(comum);
                
                System.out.println(">>> Sucesso! Participantes salvos no banco de dados");
                
                // Listar para confirmar
                repo.findAll().forEach(part -> {
                    System.out.println(">>> Participante no Banco: " + part.getNome());
                });
            }
        };
    }
}
