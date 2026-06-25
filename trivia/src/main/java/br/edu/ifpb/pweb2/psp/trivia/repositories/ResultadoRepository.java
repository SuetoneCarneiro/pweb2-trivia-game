package br.edu.ifpb.pweb2.psp.trivia.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    Page<Resultado> findByIdCorridaId(Long corridaId, Pageable pageable);

    
    List<Resultado> findByIdParticipanteId(Long participanteId);
}