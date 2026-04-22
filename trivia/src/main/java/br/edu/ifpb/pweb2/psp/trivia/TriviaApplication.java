package br.edu.ifpb.pweb2.psp.trivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriviaApplication.class, args);
	}

    // Teste para verificar se o banco de dados está funcionando corretamente
	// @Bean 
    // public CommandLineRunner testarBanco(ParticipanteRepository repo) {
    //     return args -> {
    //         System.out.println(">>> A tentar salvar participante de teste...");
            
    //         Participante p = new Participante();
    //         p.setNome("Pedro Lucas");
    //         p.setEmail("pepe@gmail.com");
    //         p.setAdm(true);
            
    //         repo.save(p); // O JPA gera o INSERT sozinho
            
    //         System.out.println(">>> Sucesso! Participante salvo com ID: " + p.getId());
            
    //         // Listar para confirmar
    //         repo.findAll().forEach(part -> {
    //             System.out.println(">>> Participante no Banco: " + part.getNome());
    //         });
    //     };
    // }
}
