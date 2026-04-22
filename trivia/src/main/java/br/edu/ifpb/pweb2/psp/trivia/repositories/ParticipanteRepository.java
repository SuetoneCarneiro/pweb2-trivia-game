package br.edu.ifpb.pweb2.psp.trivia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.psp.trivia.entities.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

}
