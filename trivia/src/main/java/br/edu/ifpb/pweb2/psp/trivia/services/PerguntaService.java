package br.edu.ifpb.pweb2.psp.trivia.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.pweb2.psp.trivia.entities.Pergunta;
import br.edu.ifpb.pweb2.psp.trivia.repositories.PerguntaRepository;

@Service
public class PerguntaService {
    @Autowired
    private PerguntaRepository perguntaRepository;

    @Autowired
    private ImagemUploadService imagemUploadService;

    public List<Pergunta> listarPorCorrida(Long idCorrida) {
        return perguntaRepository.findAll().stream()
                .filter(p -> p.getIdCorrida() != null && p.getIdCorrida().getId().equals(idCorrida))
                .toList();
    }

    public Pergunta buscarPorId(Long id) {
        return perguntaRepository.findById(id).orElse(null);
    }

    public Pergunta salvar(Pergunta pergunta, MultipartFile imagem) throws IOException {
        Pergunta existente = pergunta.getId() != null ? buscarPorId(pergunta.getId()) : null;

        if (imagem != null && !imagem.isEmpty()) {
            if (existente != null && existente.getUrlImagem() != null) {
                imagemUploadService.excluirPorUrl(existente.getUrlImagem());
            }
            pergunta.setUrlImagem(imagemUploadService.salvarPng(imagem));
        } else if (existente != null) {
            pergunta.setUrlImagem(existente.getUrlImagem());
        }

        return perguntaRepository.save(pergunta);
    }

    public void excluir(Long id) throws IOException {
        Pergunta pergunta = buscarPorId(id);
        if (pergunta == null) {
            return;
        }
        imagemUploadService.excluirPorUrl(pergunta.getUrlImagem());
        perguntaRepository.deleteById(id);
    }
}