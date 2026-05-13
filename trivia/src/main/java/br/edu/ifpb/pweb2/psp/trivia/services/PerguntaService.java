package br.edu.ifpb.pweb2.psp.trivia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import br.edu.ifpb.pweb2.psp.trivia.entities.Pergunta;
import br.edu.ifpb.pweb2.psp.trivia.repositories.PerguntaRepository;

@Service
public class PerguntaService {
    @Autowired
    private PerguntaRepository perguntaRepository;

    public List<Pergunta> listarPorCorrida(Long idCorrida) {
        return perguntaRepository.findAll().stream()
                .filter(p -> p.getIdCorrida() != null && p.getIdCorrida().getId().equals(idCorrida))
                .toList();
    }

    public Pergunta buscarPorId(Long id) {
        return perguntaRepository.findById(id).orElse(null);
    }

    public Pergunta salvar(Pergunta pergunta) {
        return perguntaRepository.save(pergunta);
    }

    public void excluir(Long id) {
        perguntaRepository.deleteById(id);
    }
}