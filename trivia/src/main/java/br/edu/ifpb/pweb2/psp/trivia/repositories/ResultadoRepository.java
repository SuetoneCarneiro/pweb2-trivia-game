package br.edu.ifpb.pweb2.psp.trivia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

}
