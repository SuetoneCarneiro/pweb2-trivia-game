package br.edu.ifpb.pweb2.psp.trivia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import br.edu.ifpb.pweb2.psp.trivia.entities.Resultado;
import br.edu.ifpb.pweb2.psp.trivia.repositories.ResultadoRepository;

@Service
public class ResultadoService {
    @Autowired
    private ResultadoRepository resultadoRepository;

    
    public Resultado salvar(Resultado resultado) {
        return resultadoRepository.save(resultado);
    }

    public List<Resultado> listarTodos() {
        return resultadoRepository.findAll();
    }

    public List<Resultado> listarRankingGeral() {
        return resultadoRepository.findAll().stream()
                .sorted((r1, r2) -> r2.getPontuacao().compareTo(r1.getPontuacao()))
                .toList();
    }

    public List<Resultado> listarRankingPorCorrida(Long idCorrida) {
        return resultadoRepository.findAll().stream()
                .filter(r -> r.getIdCorrida() != null && r.getIdCorrida().getId().equals(idCorrida))
                .sorted((r1, r2) -> r2.getPontuacao().compareTo(r1.getPontuacao()))
                .toList();
    }

    public List<Resultado> listarPorParticipante(Long idParticipante) {
        return resultadoRepository.findByIdParticipanteId(idParticipante);
    }

    //MÉTODOS PARA PAGINAÇÃOp
    public Page<Resultado> listarRankingGeralPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("pontuacao").descending());
        return resultadoRepository.findAll(pageable);
    }

    public Page<Resultado> listarRankingPorCorridaPaginado(Long corridaId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("pontuacao").descending());
        return resultadoRepository.findByIdCorridaId(corridaId, pageable);
    }
}