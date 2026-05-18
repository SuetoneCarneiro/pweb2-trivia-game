package br.edu.ifpb.pweb2.psp.trivia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    Optional<Participante> findByNomeIgnoreCase(String nome);
}
