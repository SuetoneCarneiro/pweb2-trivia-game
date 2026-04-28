package br.edu.ifpb.pweb2.psp.trivia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
}